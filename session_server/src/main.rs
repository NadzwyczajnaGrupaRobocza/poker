#![feature(proc_macro_hygiene, decl_macro)]

#[macro_use]
extern crate rocket;
#[macro_use]
extern crate rocket_contrib;

use rocket::State;
use std::collections::HashMap;
use std::sync::Mutex;

use rocket_contrib::json::{Json, JsonValue};

pub use session_server::db_types::db;

type UsersDB = HashMap<db::UserID, db::UserData>;
type SessionsDB = HashMap<db::SessionID, db::SessionData>;
type PendingSessionDB = HashMap<db::SessionID, db::PendingSessionData>;

struct ServerDB {
    users_db: UsersDB,
    session_db: SessionsDB,
    pending_session_db: PendingSessionDB,
}

impl ServerDB {
    pub fn new() -> ServerDB {
        ServerDB {
            users_db: UsersDB::new(),
            session_db: SessionsDB::new(),
            pending_session_db: PendingSessionDB::new(),
        }
    }
}

type MutexServerDB = Mutex<ServerDB>;

#[get("/")]
fn index() -> &'static str {
    "Hello, world!"
}

// example: curl -d "{\"peers\": [\"user-id-2\", \"user-id-3\", \"user-id-4\"]}" -v -X PUT -H "Content-Type:application/json" http://localhost:8000/user/user-id-1/known_peers
#[put("/user/<id>/known_peers", format = "json", data = "<peers>")]
fn update_known_peers(
    id: String,
    peers: Json<db::KnownPeers>,
    db: State<'_, MutexServerDB>,
) -> JsonValue {
    let mut db = db.lock().unwrap();
    let peers = peers.into_inner();
    println!("updated peers for {} to {:?}", id, peers);
    let users_entry = db
        .users_db
        .entry(db::UserID::new(&id))
        .or_insert_with(db::UserData::new);
    users_entry.known_peers = peers;

    json!({ "status": "ok" })
}

// example: curl -v -X GET -H "Content-Type:application/json" http://localhost:8000/user/user-id-1/known_peers
#[get("/user/<id>/known_peers")]
fn get_known_peers(id: String, db: State<'_, MutexServerDB>) -> JsonValue {
    let db = db.lock().unwrap();
    if db.users_db.contains_key(&db::UserID::new(&id)) {
        json!({ "status" : "ok", })
    } else {
        json!({ "status" : "not found" })
    }
}

// example: curl -d "{\"readable_name\": \"jakas nazwa\", \"creator_id\": \"ID-dummy\", \"other_participants\": [\"user-id-2\", \"user-id-3\", \"user-id-4\"]}" -v -X PUT -H "Content-Type:application/json" http://localhost:8000/session/create
#[put("/session/create", format = "json", data = "<req>")]
fn create_session(req: Json<db::CreateSessionReq>, db: State<'_, MutexServerDB>) -> JsonValue {
    let session_id = db::SessionID::make_new();
    let mut db = db.lock().unwrap();

    let req = req.into_inner();

    let session_data = db::PendingSessionData {
        readable_name: req.readable_name.clone(),
        unconfirmed_participants: req
            .other_participants
            .iter()
            .map(&db::UserID::new)
            .collect(),
        confirmed_participants: vec![db::UserID::new(&req.creator_id)],
    };

    let unconfirmed_participants = &session_data.unconfirmed_participants;

    // Returns Option<V> which is empty if key is new and contains old value if key is already present.
    // Mayby a better add method should be used? Adding a check for session being empty would be nice.
    // https://doc.rust-lang.org/std/collections/struct.HashMap.html#method.insert
    let _session = db
        .pending_session_db
        .insert(session_id.clone(), session_data.clone());

    for participant in unconfirmed_participants.iter() {
        let join_req = db::JoinSessionReq {
            readable_name: req.readable_name.clone(),
            creator_id: req.creator_id.clone(),
            session_id: session_id.clone(),
        };

        match serde_json::to_string(&join_req) {
            Ok(json_str) => match db.users_db.get_mut(&participant) {
                Some(user_data) => user_data.message_queue.append(json_str),
                None => println!("error"), // cause we all love meaningful errors
            },
            Err(_) => println!("error"),
        }
    }

    json!({"status": "ok",
           "session_id": session_id })
}

#[get("/user/<id>/queue")]
fn get_messages_for(id: String, db: State<'_, MutexServerDB>) -> JsonValue {
    let mut db = db.lock().unwrap();
    match db.users_db.get_mut(&db::UserID::new(&id)) {
        Some(user_data) => {
            // TODO: figure out how to do a swap here
            let current_message_queue = user_data.message_queue.clone();
            user_data.message_queue = db::MessageQueue::new();
            match serde_json::to_value(current_message_queue) {
                Ok(json_value) => JsonValue::from(json_value),
                Err(_) => json!({"status": "failed to serialize"}),
            }
        }
        None => json!({
            "status": "user not found",
        }),
    }
}

#[put("/user/<user_id>/join/<session_id>")]
fn join_session(session_id: String, user_id: String, db: State<'_, MutexServerDB>) -> JsonValue {
    let session_id = db::SessionID::make(&session_id);
    let user_id = db::UserID::new(&user_id);
    let mut db = db.lock().unwrap();

    match db.pending_session_db.get_mut(&session_id) {
        Some(session_data) => {
            if !session_data.unconfirmed_participants.contains(&user_id) {
                return json!({"status" : "user not in the session!"});
            }
            session_data
                .unconfirmed_participants
                .retain(|element| element != &user_id);

            session_data.confirmed_participants.push(user_id);

            if session_data.unconfirmed_participants.is_empty() {
                match db.pending_session_db.remove(&session_id) {
                    Some(session_data) => {
                        let confirmed_session_data = db::SessionData {
                            readable_name: session_data.readable_name,
                            participants: session_data.confirmed_participants,
                        };
                        db.session_db.insert(session_id, confirmed_session_data);
                    }
                    None => return json!({"status":"internal error"}),
                }
            }

            return json!({"status" : "ok"});
        }
        None => return json!({
            "status" : "session not found!"
        }),
    }
}

#[put("/session/<session_id>/broadcast", format = "json", data = "<msg>")]
fn session_broadcast(session_id: String, msg : String, db: State<'_, MutexServerDB>) -> JsonValue {
    let session_id = db::SessionID::make(&session_id);
    let mut db = db.lock().unwrap();
    let session_db = &db.session_db;
    let mut users_db = &mut db.users_db;

    match session_db.get(&session_id) {
        Some(session) => {
            for participant_id in session.participants.iter() {
                match users_db.get_mut(&participant_id) {
                    Some(user) => {
                        user.message_queue.append(msg.clone())
                    }
                    None => print!("broadcast: user not found")
                }
            }
            json!({"status" : "ok"})
        },
        None => json!({"status" : "session not found"})
    }
}

fn main() {
    println!("rocket launch?");
    rocket::ignite()
        .mount(
            "/",
            routes![
                index,
                get_known_peers,
                update_known_peers,
                create_session,
                get_messages_for,
                join_session,
                session_broadcast,
            ],
        )
        .manage(Mutex::new(ServerDB::new()))
        .launch();
}

#![feature(proc_macro_hygiene, decl_macro)]

#[macro_use]
extern crate rocket;
#[macro_use]
extern crate rocket_contrib;

use rocket::State;
use std::collections::HashMap;
use std::sync::Mutex;

use rocket_contrib::json::{Json, JsonValue};
use uuid::Uuid;

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

#[put("/user/<id>/known_peers", format = "json", data = "<peers>")]
fn update_known_peers(
    id: String,
    peers: Json<db::KnownPeers>,
    db: State<'_, MutexServerDB>,
) -> JsonValue {
    let mut db = db.lock().unwrap();
    println!("updated peers for {} to {:?}", id, peers.0.peers);
    let users_entry = db
        .users_db
        .entry(db::UserID::new(&id))
        .or_insert_with(db::UserData::new);
    let peers = peers.into_inner();
    users_entry.known_peers.peers = peers.peers;

    json!({ "status": "ok" })
}

#[get("/user/<id>/known_peers")]
fn get_known_peers(id: String, db: State<'_, MutexServerDB>) -> JsonValue {
    let db = db.lock().unwrap();
    if db.users_db.contains_key(&db::UserID::new(&id)) {
        json!({ "status" : "ok", })
    } else {
        json!({ "status" : "not found" })
    }
}

#[put("/session/create", format = "json", data = "<req>")]
fn create_session(req: Json<db::CreateSessionReq>, db: State<'_, MutexServerDB>) -> JsonValue {
    let session_id = db::SessionID::make_new();
    let mut db = db.lock().unwrap();

    let req = req.into_inner();

    let session_data = db::PendingSessionData {
        readable_name: req.readable_name.clone(),
        unconfirmed_participants: req.other_participants.iter().map(&db::UserID::new).collect(),
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
                Some(user_data) => user_data.message_queue.push(json_str),
                None => print!("error"), // cause we all love meaningful errors
            }
            Err(_) => println!("error")
        }
    }

    json!({"status": "ok",
           "session_id": session_id })
}

fn main() {
    println!("rocket launch?");
    rocket::ignite()
        .mount(
            "/",
            routes![index, get_known_peers, update_known_peers, create_session],
        )
        .manage(Mutex::new(ServerDB::new()))
        .launch();
}

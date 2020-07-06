#![feature(proc_macro_hygiene, decl_macro)]

#[macro_use]
extern crate rocket;
#[macro_use]
extern crate rocket_contrib;
#[macro_use]
extern crate serde_derive;

use rocket::State;
use std::collections::HashMap;
use std::sync::Mutex;

use rocket_contrib::json::{Json, JsonValue};
use uuid::Uuid;

#[derive(PartialEq, Eq, Hash, Clone)]
struct UserID {
    user_id: String,
}

impl UserID {
    pub fn new(id: &String) -> UserID {
        UserID {
            user_id: id.clone(),
        }
    }
}

struct UserData {
    known_peers: KnownPeers,
    active_sessions: Vec<SessionID>,
}

impl UserData {
    pub fn new() -> UserData {
        UserData {
            known_peers: KnownPeers::new(),
            active_sessions: vec![],
        }
    }
}

#[derive(Serialize, Deserialize)]
struct KnownPeers {
    peers: Vec<String>,
}

impl KnownPeers {
    pub fn new() -> KnownPeers {
        KnownPeers { peers: vec![] }
    }
}

#[derive(PartialEq, Eq, Hash, Serialize, Deserialize, Clone)]
struct SessionID {
    session_id: String,
}

impl SessionID {
    pub fn make_new() -> SessionID {
        let id = Uuid::new_v4();
        id.simple();
        SessionID {
            session_id: format!("{}", id.simple()),
        }
    }
}

struct PendingSessionData {
    unconfirmed_participants: Vec<UserID>,
    confirmed_participants: Vec<UserID>,
}

struct SessionData {
    participants: Vec<UserID>,
}

type UsersDB = HashMap<UserID, UserData>;
type SessionsDB = HashMap<SessionID, SessionData>;
type PendingSessionDB = HashMap<SessionID, PendingSessionData>;

struct ServerDB {
    users_db: UsersDB,
    session_db: SessionsDB,
    pending_session_db: PendingSessionDB,
}

impl ServerDB {
    pub fn new() -> ServerDB {
        ServerDB{ users_db: UsersDB::new(),
                  session_db: SessionsDB::new(),
                  pending_session_db: PendingSessionDB::new(),
        }
    }
}

type MutexServerDB = Mutex<ServerDB>;

#[derive(Serialize,Deserialize)]
struct CreateSessionReq {
    creator_id: String,
    other_participants: Vec<String>,
}

#[get("/")]
fn index() -> &'static str {
    "Hello, world!"
}

#[put("/user/<id>/known_peers", format = "json", data = "<peers>")]
fn update_known_peers(
    id: String,
    peers: Json<KnownPeers>,
    db: State<'_, MutexServerDB>,
) -> JsonValue {
    let mut db = db.lock().unwrap();
    println!("updated peers for {} to {:?}", id, peers.0.peers);
    let users_entry = db.users_db.entry(UserID::new(&id)).or_insert(UserData::new());
    users_entry.known_peers.peers = peers.0.peers;

    json!({ "status": "ok" })
}

#[get("/user/<id>/known_peers")]
fn get_known_peers(id: String, db: State<'_, MutexServerDB>) -> JsonValue {
    let db = db.lock().unwrap();
    if db.users_db.contains_key(&UserID::new(&id)) {
        json!({ "status" : "ok", })
    } else {
        json!({ "status" : "not found" })
    }
}

#[put("/session/create", format="json", data = "<req>")]
fn create_session(req: Json<CreateSessionReq>, db: State<'_, MutexServerDB>) -> JsonValue {
    let session_id = SessionID::make_new();
    let mut db = db.lock().unwrap();
    let session_data = PendingSessionData {
        unconfirmed_participants: req.0.other_participants.iter().map(&UserID::new).collect(),
        confirmed_participants: vec![UserID::new(&req.0.creator_id)] };
    // Returns Option<V> which is empty if key is new and contains old value if key is already present.
    // Mayby a better add method should be used? Adding a check for session being empty would be nice.
    // https://doc.rust-lang.org/std/collections/struct.HashMap.html#method.insert
    let _session = db.pending_session_db.insert(session_id.clone(), session_data);


    json!({"status": "ok",
           "session_id": session_id })
}

fn main() {
    println!("rocket launch?");
    rocket::ignite()
        .mount("/", routes![index, get_known_peers, update_known_peers, create_session])
        .manage(Mutex::new(ServerDB::new()))
        .launch();
}

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

struct UserData {
    known_peers: KnownPeers,
    active_sessions: Vec<String>,
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

type UsersDatabase = Mutex<HashMap<String, UserData>>;

#[get("/")]
fn index() -> &'static str {
    "Hello, world!"
}

#[put("/user/<id>/known_peers", format = "json", data = "<peers>")]
fn update_known_peers(
    id: String,
    peers: Json<KnownPeers>,
    db: State<'_, UsersDatabase>,
) -> JsonValue {
    let user_id: String = String::from("user:") + &id;

    let mut db = db.lock().unwrap();
    println!("updated peers for {} to {:?}", user_id, peers.0.peers);
    let users_entry = db.entry(user_id).or_insert(UserData::new());
    users_entry.known_peers.peers = peers.0.peers;

    json!({ "status": "ok" })
}

#[get("/user/<id>/known_peers")]
fn get_known_peers(id: String, db: State<'_, UsersDatabase>) -> JsonValue {
    let user_id: String = String::from("user:") + &id;

    let mut db = db.lock().unwrap();
    if db.contains_key(&user_id) {
        json!({ "status" : "ok", })
    } else {
        json!({ "status" : "not found" })
    }
}

fn main() {
    println!("rocket launch?");
    rocket::ignite()
        .mount("/", routes![index, get_known_peers, update_known_peers])
        .manage(Mutex::new(HashMap::<String, UserData>::new()))
        .launch();
}

pub mod db {
    use uuid::Uuid;

    #[derive(PartialEq, Eq, Hash, Clone)]
    pub struct UserID {
        user_id: String,
    }

    impl UserID {
        pub fn new(id: &String) -> UserID {
            UserID {
                user_id: id.clone(),
            }
        }
    }

    pub struct UserData {
        pub known_peers: KnownPeers,
        pub active_sessions: Vec<SessionID>,
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
    pub struct KnownPeers {
        pub peers: Vec<String>,
    }

    impl KnownPeers {
        pub fn new() -> KnownPeers {
            KnownPeers { peers: vec![] }
        }
    }

    #[derive(PartialEq, Eq, Hash, Serialize, Deserialize, Clone)]
    pub struct SessionID {
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

    // TODO: remove pub on members here - there's an invariant to preserve with this one
    pub struct PendingSessionData {
        pub unconfirmed_participants: Vec<UserID>,
        pub confirmed_participants: Vec<UserID>,
    }

    pub struct SessionData {
        participants: Vec<UserID>,
    }

    #[derive(Serialize, Deserialize)]
    pub struct CreateSessionReq {
        pub creator_id: String,
        pub other_participants: Vec<String>,
    }
}

pub mod db {
    use uuid::Uuid;

    #[derive(PartialEq, Eq, Hash, Clone)]
    pub struct UserID {
        user_id: String,
    }

    impl UserID {
        pub fn new(id: &String) -> UserID {
            UserID {
                user_id: id.to_string(),
            }
        }
    }

    #[derive(Serialize, Deserialize, Default, Clone)]
    pub struct MessageQueue {
        data: Vec<String>,
    }

    impl MessageQueue {
        pub fn new() -> MessageQueue {
            MessageQueue { data: vec![] }
        }
        pub fn append(&mut self, msg: String) {
            self.data.push(msg)
        }
    }

    #[derive(Default)]
    pub struct UserData {
        pub known_peers: KnownPeers,
        pub active_sessions: Vec<SessionID>,
        pub message_queue: MessageQueue,
    }

    impl UserData {
        pub fn new() -> UserData {
            UserData {
                known_peers: KnownPeers::new(),
                active_sessions: vec![],
                message_queue: MessageQueue::new(),
            }
        }
    }

    #[derive(Serialize, Deserialize, Default, Debug)]
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
        // 'new' function has special meaning, trying to avoid it, but this may need revisiting
        pub fn make_new() -> SessionID {
            let id = Uuid::new_v4();
            id.simple();
            SessionID {
                session_id: format!("{}", id.simple()),
            }
        }
        pub fn make(id: &str) -> SessionID {
            SessionID {
                session_id: id.to_string(),
            }
        }
    }

    // TODO: remove pub on members here - there's an invariant to preserve with this one
    #[derive(Clone)]
    pub struct PendingSessionData {
        pub readable_name: String,
        pub unconfirmed_participants: Vec<UserID>,
        pub confirmed_participants: Vec<UserID>,
    }

    #[derive(Clone)]
    pub struct SessionData {
        pub readable_name: String,
        pub participants: Vec<UserID>,
    }

    #[derive(Serialize, Deserialize)]
    pub struct CreateSessionReq {
        pub readable_name: String, // as in 'visible to the end-user'
        pub creator_id: String,
        pub other_participants: Vec<String>,
    }

    #[derive(Serialize, Deserialize)]
    pub struct JoinSessionReq {
        pub readable_name: String,
        pub creator_id: String,
        pub session_id: SessionID,
    }
}

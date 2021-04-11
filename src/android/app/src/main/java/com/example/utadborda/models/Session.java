package com.example.utadborda.models;

import java.util.UUID;

public class Session {

    private UUID id;
    private String session;

    public Session(UUID id, String session){
        this.id = id;
        this.session = session;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}

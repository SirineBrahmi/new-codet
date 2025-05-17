package com.example.pfe;

public class ChatRoom {
    private String formationId;

    public ChatRoom() {
        // Required for Firebase
    }

    public ChatRoom(String formationId) {
        this.formationId = formationId;
    }

    public String getFormationId() {
        return formationId;
    }

    public void setFormationId(String formationId) {
        this.formationId = formationId;
    }
}
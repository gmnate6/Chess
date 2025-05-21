package com.nathanholmberg.chess.server.models;

import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.serialization.MessageSerializer;
import jakarta.websocket.Session;

public class Player {
    private final Session session;
    private String username = "guest";
    private String profile = "default";

    public Player(Session session) {
        this.session = session;
    }

    public String getUsername() {
        return username;
    }
    public String getProfile() {
        return profile;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setProfile(String profile) {
        this.profile = profile;
    }

    public boolean isConnected() {
        return session.isOpen();
    }

    public void sendMessage(Message message) {
        if (!isConnected()) {
            System.err.println("Response Error: Cannot send message to disconnected player.");
            return;
        }

        try {
            session.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        } catch (Exception e) {
            System.err.println("Response Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
        }
    }

    public void close() {
        try {
            session.close();
        } catch (Exception e) {
            System.err.println("Closing Error:  " + session.getId() + " - " + e.getMessage());
        }
    }
}

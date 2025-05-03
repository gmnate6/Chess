package com.nathanholmberg.chess.protocol.messages.game;

import com.nathanholmberg.chess.protocol.messages.Message;

public class ClientInfoMessage extends Message {
    private final String username;
    private final String avatar;

    public ClientInfoMessage(String username, String avatar) {
        this.username = username;
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }
}

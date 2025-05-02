package com.nathanholmberg.chess.protocol.messages;

public abstract class Message {
    private final String type = getClass().getSimpleName();

    public String getType() {
        return type;
    }
}

package com.nathanholmberg.chess.protocol.messages.server;

import com.nathanholmberg.chess.protocol.messages.Message;

public class IllegalMoveMessage extends Message {
    private final String reason;

    public IllegalMoveMessage(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}

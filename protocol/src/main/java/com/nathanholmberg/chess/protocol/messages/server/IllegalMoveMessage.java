package com.nathanholmberg.chess.protocol.messages.server;

import com.nathanholmberg.chess.protocol.messages.Message;

public class IllegalMoveMessage extends Message {
    private final String move;

    public IllegalMoveMessage(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}

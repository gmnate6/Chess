package com.nathanholmberg.chess.protocol.messages.game;

import com.nathanholmberg.chess.protocol.messages.Message;

public class MoveMessage extends Message {
    private final String move;

    public MoveMessage(String move) {
        this.move = move;
    }

    public String getMove() {
        return move;
    }
}

package com.nathanholmberg.chess.protocol.messages.server;

import com.nathanholmberg.chess.protocol.messages.Message;

public class GameStateMessage extends Message {
    private final String pgn;

    public GameStateMessage(String pgn) {
        this.pgn = pgn;
    }

    public String getPGN() {
        return pgn;
    }
}

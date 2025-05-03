package com.nathanholmberg.chess.protocol.messages.game.server;

public class GameStateMessage extends ClockUpdateMessage {
    private final String pgn;

    public GameStateMessage(String pgn, int whiteTime, int blackTime) {
        super(whiteTime, blackTime);
        this.pgn = pgn;
    }

    public String getPGN() {
        return pgn;
    }
}

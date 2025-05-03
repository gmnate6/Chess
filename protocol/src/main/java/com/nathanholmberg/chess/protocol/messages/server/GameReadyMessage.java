package com.nathanholmberg.chess.protocol.messages.server;

import com.nathanholmberg.chess.protocol.messages.Message;

public class GameReadyMessage extends Message {
    private final String gameId;
    private final String color;

    public GameReadyMessage(String gameId, String color) {
        this.gameId = gameId;
        this.color = color;
    }

    public String getGameId() {
        return gameId;
    }

    public String getColor() {
        return color;
    }
}

package com.nathanholmberg.chess.protocol.messages.lobby.server;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.protocol.messages.Message;

public class GameReadyMessage extends Message {
    private final String gameId;
    private final Color color;

    public GameReadyMessage(String gameId, Color color) {
        this.gameId = gameId;
        this.color = color;
    }

    public String getGameId() {
        return gameId;
    }

    public Color getColor() {
        return color;
    }
}

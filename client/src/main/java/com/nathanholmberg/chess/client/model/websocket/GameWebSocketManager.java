package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;

import jakarta.websocket.CloseReason;

public class GameWebSocketManager extends WebSocketManager {
    public GameWebSocketManager(String gameId, Color color) {
        super(WebSocketEndpoints.GAME.replace("{gameId}", gameId).replace("{color}", color.toString()));
    }

    @Override
    protected void onConnected() {
    }

    @Override
    protected void onMessageReceived(String message) {
    }

    @Override
    protected void onDisconnected(CloseReason reason) {
    }

    @Override
    protected void onError(Throwable throwable) {
    }
}
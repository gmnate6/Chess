package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;

import jakarta.websocket.CloseReason;

public class LobbyWebSocketManager extends WebSocketManager {
    private LobbyWebSocketManager() {
        super(WebSocketEndpoints.PING);
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
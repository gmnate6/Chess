package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.protocol.constants.Endpoints;

import jakarta.websocket.CloseReason;

public class PingWebSocketManager extends WebSocketManager {
    private PingWebSocketManager() {
        super(Endpoints.PING);
    }

    @Override
    protected void onConnected() {}

    @Override
    protected void onMessageReceived(String message) {}

    @Override
    protected void onDisconnected(CloseReason reason) {}

    @Override
    protected void onError(Throwable throwable) {}

    public static boolean ping() {
        PingWebSocketManager pingWebSocketManager = new PingWebSocketManager();
        pingWebSocketManager.connect();
        boolean result = pingWebSocketManager.isConnected();
        pingWebSocketManager.close();
        return result;
    }
}

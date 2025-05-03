package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.server.GameReadyMessage;
import com.nathanholmberg.chess.protocol.messages.server.JoinedMatchmakingMessage;
import com.nathanholmberg.chess.protocol.serialization.MessageDeserializer;

import jakarta.websocket.CloseReason;

public class LobbyWebSocketManager extends WebSocketManager {
    public LobbyWebSocketManager() {
        super(WebSocketEndpoints.LOBBY);
    }

    @Override
    protected void onConnected() {
        System.out.println("Joined lobby.");
    }

    @Override
    protected void onMessageReceived(String message) {
        Message messageObj = MessageDeserializer.deserialize(message);

        // Handle Message
        if (messageObj instanceof JoinedMatchmakingMessage joinedMatchmakingMessage) {
            System.out.println("Joined matchmaking.");
        } else if (messageObj instanceof GameReadyMessage gameReadyMessage) {
            System.out.println("Game ready.");
        } else {
            System.out.println("Unknown message type: " + messageObj.getType());
        }
    }

    @Override
    protected void onDisconnected(CloseReason reason) {
    }

    @Override
    protected void onError(Throwable throwable) {
    }
}
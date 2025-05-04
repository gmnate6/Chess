package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessTimer;
import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.lobby.server.GameReadyMessage;
import com.nathanholmberg.chess.protocol.messages.lobby.server.JoinedMatchmakingMessage;
import com.nathanholmberg.chess.protocol.serialization.MessageDeserializer;

import jakarta.websocket.CloseReason;

public class LobbyWebSocketManager extends WebSocketManager {
    private GameReadyListener gameReadyListener;

    public LobbyWebSocketManager() {
        super(WebSocketEndpoints.LOBBY);
    }

    public interface GameReadyListener {
        void onGameReady(String gameId, Color color);
    }

    public void setGameReadyListener(GameReadyListener listener) {
        this.gameReadyListener = listener;
    }

    @Override
    protected void onConnected() {
    }

    @Override
    protected void onMessageReceived(String message) {
        Message messageObj = MessageDeserializer.deserialize(message);

        if (messageObj instanceof JoinedMatchmakingMessage) {
            return;
        }

        if (messageObj instanceof GameReadyMessage gameReadyMessage) {
            gameReadyListener.onGameReady(
                    gameReadyMessage.getGameId(),
                    gameReadyMessage.getColor()
            );
            return;
        }

        System.out.println("Unknown message type: " + messageObj.getType());
    }

    @Override
    protected void onDisconnected(CloseReason reason) {
    }

    @Override
    protected void onError(Throwable throwable) {
    }
}
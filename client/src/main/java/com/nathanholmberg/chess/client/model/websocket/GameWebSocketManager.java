package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;

import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.client.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.GameStartMessage;
import com.nathanholmberg.chess.protocol.serialization.MessageDeserializer;
import jakarta.websocket.CloseReason;

public class GameWebSocketManager extends WebSocketManager {
    private GameMessageListener gameMessageListener;

    public GameWebSocketManager(String gameId, Color color) {
        super(WebSocketEndpoints.GAME.replace("{gameId}", gameId).replace("{color}", color.toString()));
    }

    public interface GameMessageListener {
        void onClientInfoMessage(String username, String avatar);
        void onGameStartMessage();
        void onMoveMessage(String move);
    }

    public void setGameMessageListener(GameMessageListener listener) {
        this.gameMessageListener = listener;
    }

    @Override
    protected void onConnected() {
    }

    @Override
    protected void onMessageReceived(String message) {
        Message messageObj = MessageDeserializer.deserialize(message);

        if (messageObj instanceof GameStartMessage) {
            return;
        }

        if (messageObj instanceof MoveMessage moveMessage) {
            gameMessageListener.onMoveMessage(
                    moveMessage.getMove()
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
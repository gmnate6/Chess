package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;

import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.client.ResignMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.GameStartMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.IllegalMoveMessage;
import com.nathanholmberg.chess.protocol.serialization.MessageDeserializer;
import jakarta.websocket.CloseReason;

public class GameWebSocketManager extends WebSocketManager {
    private GameMessageListener gameMessageListener;

    public GameWebSocketManager(String gameId, Color color) {
        super(WebSocketEndpoints.GAME.replace("{gameId}", gameId).replace("{color}", color.toString()));
    }

    public interface GameMessageListener {
        void onClientInfoMessage(String username, String avatar);
        void onGameStartMessage(long initialTime, long increment);
        void onMoveMessage(String move);
        void onIllegalMoveMessage(String reason);
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

        if (messageObj instanceof ClientInfoMessage clientInfoMessage) {
            gameMessageListener.onClientInfoMessage(
                    clientInfoMessage.getUsername(),
                    clientInfoMessage.getAvatar()
            );
            return;
        }

        if (messageObj instanceof GameStartMessage gameStartMessage) {
            gameMessageListener.onGameStartMessage(gameStartMessage.getInitialTime(), gameStartMessage.getIncrement());
            return;
        }

        if (messageObj instanceof MoveMessage moveMessage) {
            gameMessageListener.onMoveMessage(
                    moveMessage.getMove()
            );
            return;
        }

        if (messageObj instanceof IllegalMoveMessage illegalMoveMessage) {
            gameMessageListener.onIllegalMoveMessage(
                    illegalMoveMessage.getReason()
            );
            return;
        }

        System.out.println("Unknown message type: " + messageObj.getType());
    }

    @Override
    protected void onDisconnected(CloseReason reason) {
        System.out.println("Disconnected from Game WebSocket: " + reason.getReasonPhrase());
    }

    @Override
    protected void onError(Throwable throwable) {
        System.out.println("Error in GameWebSocketManager: " + throwable.getMessage());
        throwable.printStackTrace();
    }

    public void sendClientInfoMessage(String username, String avatar) {
        Message message = new ClientInfoMessage(username, avatar);
        sendMessage(message);
    }

    public void sendMoveMessage(String move) {
        Message message = new MoveMessage(move);
        sendMessage(message);
    }

    public void sendResignMessage() {
        Message message = new ResignMessage();
        sendMessage(message);
    }
}

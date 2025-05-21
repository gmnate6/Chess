package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.enums.GameResult;
import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.client.ResignMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.*;
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
        void onDrawOfferedMessage();
        void onGameEndMessage(GameResult result);
        void onClockUpdateMessage(long whiteTime, long blackTime);
        void onGameStateMessage(String pgn);
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

        if (messageObj instanceof DrawOfferedMessage) {
            gameMessageListener.onDrawOfferedMessage();
            return;
        }

        if (messageObj instanceof GameEndMessage moveMessage) {
            gameMessageListener.onGameEndMessage(
                    moveMessage.getResult()
            );
            return;
        }

        if (messageObj instanceof ClockUpdateMessage clockUpdateMessage) {
            gameMessageListener.onClockUpdateMessage(
                    clockUpdateMessage.getWhiteTime(),
                    clockUpdateMessage.getBlackTime()
            );
        }

        if (messageObj instanceof GameStateMessage gameStateMessage) {
            gameMessageListener.onGameStateMessage(
                    gameStateMessage.getPGN()
            );
        }

        System.err.println("Unknown message type: " + messageObj.getType());
    }

    @Override
    protected void onDisconnected(CloseReason reason) {

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

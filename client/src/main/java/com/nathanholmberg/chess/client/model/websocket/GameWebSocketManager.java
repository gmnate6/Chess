package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.enums.GameResult;
import com.nathanholmberg.chess.protocol.MessageSerializer;
import com.nathanholmberg.chess.protocol.constants.Endpoints;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.client.RequestGameStateMessage;
import com.nathanholmberg.chess.protocol.messages.game.client.ResignMessage;

import com.nathanholmberg.chess.protocol.messages.game.server.*;
import jakarta.websocket.CloseReason;

public class GameWebSocketManager extends WebSocketManager {
    private GameMessageListener gameMessageListener;
    public interface GameMessageListener {
        void onClientInfoMessage(String username, String avatar);
        void onGameStartMessage(long initialTime, long increment);
        void onMoveMessage(String move);
        void onIllegalMoveMessage(String move);
        void onDrawOfferedMessage();
        void onGameEndMessage(GameResult result);
        void onClockUpdateMessage(long whiteTime, long blackTime);
        void onGameStateMessage(String pgn);
    }

    public void setGameMessageListener(GameMessageListener listener) {
        this.gameMessageListener = listener;
    }

    public GameWebSocketManager(String gameId, Color color) {
        super(Endpoints.GAME.replace("{gameId}", gameId).replace("{color}", color.toString()));
    }

    @Override
    protected void onConnected() {
    }

    @Override
    protected void onMessageReceived(String message) {
        Message messageObj = MessageSerializer.deserialize(message);

        // TODO: Consider using an interface from the protocol so the client is forced to implement every message

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
                    illegalMoveMessage.getMove()
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

        if (messageObj instanceof GameStateMessage gameStateMessage) {
            gameMessageListener.onGameStateMessage(
                    gameStateMessage.getPGN()
            );
            return;
        }

        if (messageObj instanceof ClockUpdateMessage clockUpdateMessage) {
            gameMessageListener.onClockUpdateMessage(
                    clockUpdateMessage.getWhiteTime(),
                    clockUpdateMessage.getBlackTime()
            );
            return;
        }

        System.err.println("Unknown message type: " + messageObj.getType());
    }

    @Override
    protected void onDisconnected(CloseReason reason) {

    }

    public void sendClientInfoMessage(String username, String avatar) {
        Message message = new ClientInfoMessage(username, avatar);
        sendMessage(message);
    }

    public void sendMoveMessage(String move) {
        Message message = new MoveMessage(move);
        sendMessage(message);
    }

    public void sendRequestGameStateMessage() {
        Message message = new RequestGameStateMessage();
        sendMessage(message);
    }

    public void sendResignMessage() {
        Message message = new ResignMessage();
        sendMessage(message);
    }
}

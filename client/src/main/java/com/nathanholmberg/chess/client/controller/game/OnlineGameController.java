package com.nathanholmberg.chess.client.controller.game;

import com.nathanholmberg.chess.client.model.websocket.GameWebSocketManager;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.utils.MoveUtils;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;

public class OnlineGameController extends AbstractGameController {
    GameWebSocketManager gameWebSocketManager;

    public OnlineGameController(String gameId, Color color) {
        super(color, null);
        configureOnlineGameSettings();

        gameWebSocketManager = new GameWebSocketManager(gameId, color);
        gameWebSocketManager.connect();

        // Handle Game Ready Message
        gameWebSocketManager.setGameMessageListener(new GameWebSocketManager.GameMessageListener() {
            @Override
            public void onClientInfoMessage(String username, String avatar) {
                gamePanel.setTopUsername(username);
                gamePanel.setTopAvatar(avatar);
            }

            @Override
            public void onGameStartMessage() {
                start();
            }

            @Override
            public void onMoveMessage(String move) {
                System.out.println("Client received move from server: " + move);
                try {
                    Move moveObj = MoveUtils.fromAlgebraic(move, chessGame);
                    processServerMove(moveObj);
                } catch (Exception e) {
                    onIllegalServerMove(move);
                }
            }

            @Override
            public void onIllegalMoveMessage(String reason) {
                System.out.println("Illegal move received from server: " + reason);
            }
        });
    }

    private void configureOnlineGameSettings() {
        gamePanel.drawButton.setEnabled(false); // TODO: FOR NOW
        gamePanel.rematchButton.setEnabled(false);
    }

    private void processServerMove(Move move) {
        if (!chessGame.isMoveLegal(move)) {
            onIllegalServerMove(move.toString());
            return;
        }
        executeMove(move);
        processPlayerMove(moveProcessor.getPreMove());
    }

    private void onIllegalServerMove(String move) {
        System.out.println("Illegal move received from server: " + move);
    }

    @Override
    public void executeMove(Move move) {
        if (chessGame.getTurn() == color) {
            Message message = new MoveMessage(MoveUtils.toAlgebraic(move, chessGame));
            gameWebSocketManager.sendMessage(message);
        }

        super.executeMove(move);
    }
}

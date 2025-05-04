package com.nathanholmberg.chess.client.controller.game;

import com.nathanholmberg.chess.client.model.websocket.GameWebSocketManager;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessTimer;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.utils.MoveUtils;

public class OnlineGameController extends AbstractGameController {
    GameWebSocketManager gameWebSocketManager;

    public OnlineGameController(Color color, String gameId) {
        super(color, new ChessTimer(600_000, 0));
        configureOnlineGameSettings();

        gameWebSocketManager = new GameWebSocketManager(gameId, color);

        // Handle Game Ready Message
        gameWebSocketManager.setGameMessageListener(new GameWebSocketManager.GameMessageListener() {
            @Override
            public void onClientInfoMessage(String username, String avatar) {
                gamePanel.setTopUsername(username);
                gamePanel.setTopAvatar(avatar);
            }

            @Override
            public void onGameStartMessage(long initialTime, long increment) {
                chessTimer.setInitialTime(initialTime);
                chessTimer.setIncrement(increment);
                chessTimer.setWhiteTime(initialTime);
                chessTimer.setBlackTime(initialTime);

                start();
            }

            @Override
            public void onMoveMessage(String move) {
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

        gameWebSocketManager.connect();
    }

    private void configureOnlineGameSettings() {
        gamePanel.drawButton.setEnabled(false);
        gamePanel.rematchButton.setEnabled(false);
    }

    @Override
    protected void resign() {
        super.resign();
        gameWebSocketManager.sendResignMessage();
    }

    private void processServerMove(Move move) {
        if (!chessGame.isMoveLegal(move)) {
            onIllegalServerMove(MoveUtils.toAlgebraic(move, chessGame));
            return;
        }
        executeMove(move);
        processPlayerMove(moveProcessor.getPreMove());
    }

    private void onIllegalServerMove(String move) {
        System.err.println("Illegal move received from server: " + move);
    }

    @Override
    public void executeMove(Move move) {
        if (chessGame.getTurn() == color) {
            gameWebSocketManager.sendMoveMessage(MoveUtils.toAlgebraic(move, chessGame));
        }
        super.executeMove(move);
    }
}

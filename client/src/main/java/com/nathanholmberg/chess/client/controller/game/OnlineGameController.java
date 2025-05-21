package com.nathanholmberg.chess.client.controller.game;

import com.nathanholmberg.chess.client.model.SettingsManager;
import com.nathanholmberg.chess.client.model.websocket.GameWebSocketManager;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.enums.GameResult;
import com.nathanholmberg.chess.engine.game.ChessTimer;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.utils.MoveUtils;
import com.nathanholmberg.chess.engine.utils.PGN;

public class OnlineGameController extends AbstractGameController {
    GameWebSocketManager gameWebSocketManager;

    public OnlineGameController(Color color, String gameId) {
        super(color);
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
                addTimer(new ChessTimer(initialTime, increment));
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

            @Override
            public void onDrawOfferedMessage() {
                // TODO
            }

            @Override
            public void onGameEndMessage(GameResult result) {
                if (result == chessGame.getResult()) {
                    return;
                }
                chessGame.setResult(result);

                if (inPlay) {
                    endGame();
                }
            }

            @Override
            public void onClockUpdateMessage(long whiteTime, long blackTime) {
                chessTimer.setWhiteTime(whiteTime);
                chessTimer.setBlackTime(blackTime);
            }

            @Override
            public void onGameStateMessage(String pgn) {
                if (pgn.equals(PGN.getPGN(chessGame))) {
                    return;
                }

                // Update Game State
                try {
                    chessGame = PGN.getGame(pgn);
                    if (chessTimer.getTurn() != chessGame.getTurn()) {
                        chessTimer.switchTurn();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Game State Message Received: " + pgn);
                }
            }
        });

        gameWebSocketManager.connect();
        gameWebSocketManager.sendClientInfoMessage(SettingsManager.getUsername(), SettingsManager.getAvatar());
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

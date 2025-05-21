package com.nathanholmberg.chess.server.models;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.exceptions.IllegalNotationException;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.game.ChessTimer;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.utils.MoveUtils;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.GameEndMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.GameStartMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.IllegalMoveMessage;

public class GameServer {
    private final String gameId;
    private final ChessGame chessGame;
    private final ChessTimer chessTimer;

    private Player whitePlayer;
    private Player blackPlayer;

    public GameServer(String gameId) {
        this.gameId = gameId;
        chessGame = new ChessGame();

        // Chess Timer
        chessTimer = new ChessTimer(600_000, 10_000);
        chessTimer.setListener(new ChessTimer.Listener() {
            @Override
            public void onTimerStarted(ChessTimer timer) {

            }

            @Override
            public void onTimerUpdate(ChessTimer timer) {

            }

            @Override
            public void onTimeUp(Color player) {
                chessGame.winOnTime(player.inverse());
                endGame();
            }

            @Override
            public void onTimerStopped(ChessTimer timer) {

            }
        });
    }

    public void addPlayer(Player player, Color color) throws IllegalStateException {
        if (color == Color.WHITE) {
            if (whitePlayer != null) {
                throw new IllegalStateException("White player already set.");
            }
            whitePlayer = player;
        } else {
            if (blackPlayer != null) {
                throw new IllegalStateException("Black player already set.");
            }
            blackPlayer = player;
        }

        if (whitePlayer != null && blackPlayer != null) {
            startGame();
        }
    }

    private void broadcastMessage(Message message) {
        assert whitePlayer != null;
        if (whitePlayer.isConnected()) {
            whitePlayer.sendMessage(message);
        }

        assert blackPlayer != null;
        if (blackPlayer.isConnected()) {
            blackPlayer.sendMessage(message);
        }
    }

    public Color getTurn() {
        return chessGame.getTurn();
    }

    public Player getCurrentPlayer() {
        if (getTurn() == Color.WHITE) {
            return whitePlayer;
        }
        return blackPlayer;
    }

    public void setClientInfo(Color color, String username, String avatar) {
        Message message = new ClientInfoMessage(username, avatar);

        if (color == Color.WHITE) {
            whitePlayer.setUsername(username);
            whitePlayer.setProfile(avatar);

            blackPlayer.sendMessage(message);
        } else if (color == Color.BLACK) {
            blackPlayer.setUsername(username);
            blackPlayer.setProfile(avatar);

            whitePlayer.sendMessage(message);
        }
    }

    public synchronized void resignPlayer(Color color) {
        if (!chessGame.inPlay()) {
            return;
        }
        chessGame.winByResign(color);
        endGame();
    }

    private void startGame() {
        chessTimer.start();

        // Notify players about others info
        Message message;
        message = new ClientInfoMessage(
                blackPlayer.getUsername(),
                blackPlayer.getProfile()
        );
        whitePlayer.sendMessage(message);
        message = new ClientInfoMessage(
                whitePlayer.getUsername(),
                whitePlayer.getProfile()
        );
        blackPlayer.sendMessage(message);

        // Notify players that the game has started
        message = new GameStartMessage(
                chessTimer.getInitialTime(),
                chessTimer.getIncrement()
        );
        broadcastMessage(message);
    }

    public void makeMove(String move) {
        Message message = new IllegalMoveMessage(move);

        Move moveObj;
        try {
            moveObj = MoveUtils.fromAlgebraic(move, chessGame);
        } catch (IllegalNotationException e) {
            getCurrentPlayer().sendMessage(message);
            return;
        }

        if (!chessGame.isMoveLegal(moveObj)) {
            getCurrentPlayer().sendMessage(message);
            return;
        }

        if (!chessTimer.isActive()) {
            return;
        }

        chessGame.move(moveObj);
        chessTimer.switchTurn();

        // Update the other player
        message = new MoveMessage(move);
        getCurrentPlayer().sendMessage(message);

        if (!chessGame.inPlay()) {
            endGame();
        }
    }

    public synchronized void endGame() {
        GameEndMessage message = new GameEndMessage(chessGame.getResult());
        broadcastMessage(message);

        // Close sessions
        assert whitePlayer != null;
        if (whitePlayer.isConnected()) {
            whitePlayer.close();
        }
        assert blackPlayer != null;
        if (blackPlayer.isConnected()) {
            blackPlayer.close();
        }

        chessTimer.stop();
        GameManager.getInstance().removeGame(gameId);
    }
}

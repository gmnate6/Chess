package com.nathanholmberg.chess.server.models;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.game.ChessTimer;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.utils.MoveUtils;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.GameEndMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.GameStartMessage;
import com.nathanholmberg.chess.protocol.serialization.MessageSerializer;

import jakarta.websocket.Session;

import java.io.IOException;

public class GameServer {
    private final String gameId;
    private ChessGame chessGame;
    private ChessTimer chessTimer;

    private Session whitePlayer;
    private String whitePlayerUsername = "guest";
    private String whitePlayerProfile = "default";

    private Session blackPlayer;
    private String blackPlayerUsername = "guest";
    private String blackPlayerProfile = "default";

    public GameServer(String gameId) {
        this.gameId = gameId;
    }

    public void addPlayer(Session player, Color color) {
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

    private void startGame() {
        chessGame = new ChessGame();

        // Chess Timer
        chessTimer = new ChessTimer(600_000, 10000);
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
        chessTimer.start();

        // Notify players about others info
        Message message;
        message = new ClientInfoMessage(blackPlayerUsername, blackPlayerProfile);
        whitePlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        message = new ClientInfoMessage(whitePlayerUsername, whitePlayerProfile);
        blackPlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));

        // Notify players that the game has started
        message = new GameStartMessage(
                chessTimer.getInitialTime(),
                chessTimer.getIncrement()
        );
        broadcastMessage(message);
    }

    public synchronized void handlePlayerDisconnect(Color color) {
        if (!chessGame.inPlay()) {
            return;
        }

        chessGame.winByResign(color.inverse());
        endGame();
    }

    public synchronized void endGame() {
        GameEndMessage message = new GameEndMessage(chessGame.getResult());
        broadcastMessage(message);

        // Close sessions
        try {
            if (isWhiteConnected()) {
                whitePlayer.close();
            }
            if (isBlackConnected()) {
                blackPlayer.close();
            }
        } catch (IOException e) {
            System.err.println("GameServer Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
        }

        chessTimer.stop();
        GameManager.getInstance().removeGame(gameId);
    }

    public void setClientInfo(Color color, String username, String avatar) {
        Message message = new ClientInfoMessage(username, avatar);

        if (color == Color.WHITE) {
            this.whitePlayerUsername = username;
            this.whitePlayerProfile = avatar;

            blackPlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        } else if (color == Color.BLACK) {
            this.blackPlayerUsername = username;
            this.blackPlayerProfile = avatar;

            whitePlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        }
    }

    private void broadcastMessage(Message message) {
        if (isWhiteConnected()) {
            whitePlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        }
        if (isBlackConnected()) {
            blackPlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        }
    }

    public Color getTurn() {
        return chessGame.getTurn();
    }

    public boolean isWhiteConnected() {
        return whitePlayer != null && whitePlayer.isOpen();
    }

    public boolean isBlackConnected() {
        return blackPlayer != null && blackPlayer.isOpen();
    }

    public boolean isMoveLegal(String move) {
        Move moveObj;
        try {
            moveObj = MoveUtils.fromAlgebraic(move, chessGame);
        } catch (Exception e) {
            return false;
        }
        return chessGame.isMoveLegal(moveObj);
    }

    public void makeMove(String move) {
        Move moveObj = MoveUtils.fromAlgebraic(move, chessGame);

        chessGame.move(moveObj);
        chessTimer.switchTurn();

        // Update other player
        Message message = new MoveMessage(move);
        if (getTurn() == Color.WHITE) {
            whitePlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        } else if (getTurn() == Color.BLACK) {
            blackPlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        }

        if (!chessGame.inPlay()) {
            endGame();
        }
    }
}

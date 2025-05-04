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

public class ServerGame {
    private final String gameId;
    private ChessGame chessGame;
    private ChessTimer chessTimer;
    private Thread timerThread;

    private Session whitePlayer;
    private String whitePlayerUsername = "guest";
    private String whitePlayerProfile = "default";

    private Session blackPlayer;
    private String blackPlayerUsername = "guest";
    private String blackPlayerProfile = "default";

    public ServerGame(String gameId) {
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
        chessTimer = new ChessTimer(6_000, 0);
        Message message;

        // Notify players about others info
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

        // Timer Tread
        timerThread = new Thread(() -> {
            while (true) {
                try {
                    if (chessTimer.isOutOfTime(Color.WHITE)) {
                        chessGame.winOnTime(Color.WHITE);
                        endGame();
                        break;
                    }
                    if (chessTimer.isOutOfTime(Color.BLACK)) {
                        chessGame.winOnTime(Color.WHITE);
                        endGame();
                        break;
                    }
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    System.err.println("ServerGame Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
                }
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();
    }

    public void endGame() {
        GameEndMessage message = new GameEndMessage(chessGame.getResult());
        broadcastMessage(message);

        // Close sessions
        try {
            if (whitePlayer != null) {
                whitePlayer.close();
            }
            if (blackPlayer != null) {
                blackPlayer.close();
            }
        } catch (IOException e) {
            System.err.println("ServerGame Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
        }

        timerThread.interrupt();
        GameManager.getInstance().removeGame(gameId);
    }

    public void handlePlayerDisconnect(Color color) {
        if (color == Color.WHITE && whitePlayer == null) {
            throw new IllegalStateException("White player already removed.");
        } else if (color == Color.BLACK && blackPlayer == null) {
            throw new IllegalStateException("Black player already removed.");
        }

        chessGame.winByResign(color.inverse());
        endGame();
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
        if (whitePlayer != null) {
            whitePlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        }
        if (blackPlayer != null) {
            blackPlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        }
    }

    public Color getTurn() {
        return chessGame.getTurn();
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

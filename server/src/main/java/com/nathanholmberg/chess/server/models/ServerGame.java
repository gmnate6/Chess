package com.nathanholmberg.chess.server.models;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.utils.MoveUtils;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.GameEndMessage;
import com.nathanholmberg.chess.protocol.messages.game.server.GameStartMessage;
import com.nathanholmberg.chess.protocol.serialization.MessageSerializer;

import jakarta.websocket.Session;

public class ServerGame {
    private final String gameId;
    private ChessGame chessGame;

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

        // Notify players about others info
        // TODO: Send player info to each other

        // Notify players that the game has started
        GameStartMessage message = new GameStartMessage();
        broadcastMessage(message);
    }

    public void endGame() {
        GameEndMessage message = new GameEndMessage(chessGame.getResult());
        broadcastMessage(message);

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
    }
}

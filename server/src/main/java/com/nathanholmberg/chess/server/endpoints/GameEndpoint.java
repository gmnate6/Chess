package com.nathanholmberg.chess.server.endpoints;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;
import com.nathanholmberg.chess.protocol.exceptions.ProtocolException;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.client.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.client.ResignMessage;
import com.nathanholmberg.chess.protocol.messages.server.IllegalMoveMessage;
import com.nathanholmberg.chess.protocol.messages.server.MoveAcceptedMessage;
import com.nathanholmberg.chess.protocol.serialization.MessageDeserializer;
import com.nathanholmberg.chess.protocol.serialization.MessageSerializer;
import com.nathanholmberg.chess.server.models.Game;
import com.nathanholmberg.chess.server.models.GameManager;

import com.google.gson.JsonSyntaxException;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;

@ServerEndpoint(WebSocketEndpoints.GAME)
public class GameEndpoint {
    private static final GameManager gameManager = GameManager.getInstance();
    private Session session;
    private Game game;
    private Color color;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") String gameId, @PathParam("color") String color) {
        System.out.println("Joined Game: " + session.getId());
        this.session = session;

        // Get Color
        try {
            this.color = Color.fromString(color);
        } catch (Exception e) {
            closeWithError(session, "Invalid color parameter");
            return;
        }

        // Validate game exists
        this.game = gameManager.getGame(gameId);
        if (game == null) {
            closeWithError(session, "Game not found");
            return;
        }

        // Create and add player to game
        game.addPlayer(session, this.color);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        if (game == null || color == null) {
            closeWithError(session, "Game not properly initialized");
            return;
        }

        // Handle Message
        try {
            Message messageObj = MessageDeserializer.deserialize(message);

            if (messageObj instanceof MoveMessage moveMessage) {
                if (game.getTurn() != color) {
                    sendIllegalMoveError("Not your turn");
                    return;
                }

                if (!game.isMoveLegal(moveMessage.getMove())) {
                    sendIllegalMoveError("Invalid move");
                    return;
                }

                game.makeMove(moveMessage.getMove());
                MoveAcceptedMessage moveAcceptedMessage = new MoveAcceptedMessage();
                session.getAsyncRemote().sendText(MessageSerializer.serialize(moveAcceptedMessage));
            } else if (messageObj instanceof ResignMessage) {
                game.handlePlayerDisconnect(color);
                session.close();
            } else {
                throw new Exception();
            }
        } catch (ProtocolException | JsonSyntaxException e) {
            closeWithError(session, "Invalid message format: " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        if (game != null) {
            game.handlePlayerDisconnect(color);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    private void sendIllegalMoveError(String message) {
        try {
            IllegalMoveMessage illegalMoveMessage = new IllegalMoveMessage(message);
            session.getAsyncRemote().sendText(MessageSerializer.serialize(illegalMoveMessage));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void closeWithError(Session session, String message) {
        try {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    message
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

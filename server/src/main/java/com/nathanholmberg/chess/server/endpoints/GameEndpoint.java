package com.nathanholmberg.chess.server.endpoints;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;
import com.nathanholmberg.chess.protocol.exceptions.ProtocolException;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.client.*;
import com.nathanholmberg.chess.protocol.messages.game.server.IllegalMoveMessage;
import com.nathanholmberg.chess.protocol.serialization.MessageDeserializer;
import com.nathanholmberg.chess.protocol.serialization.MessageSerializer;
import com.nathanholmberg.chess.server.models.GameServer;
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
    private GameServer gameServer;
    private Color color;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") String gameId, @PathParam("color") String color) {
        System.out.println("Joined GameServer: " + session.getId());
        this.session = session;

        // Get Color
        try {
            this.color = Color.fromString(color);
        } catch (Exception e) {
            closeWithError(session, "Invalid color parameter");
            return;
        }

        // Validate gameServer exists
        this.gameServer = gameManager.getGame(gameId);
        if (gameServer == null) {
            closeWithError(session, "GameServer not found");
            return;
        }

        // Create and add player to gameServer
        try {
            gameServer.addPlayer(session, this.color);
        } catch (Exception e) {
            closeWithError(session, e.getMessage());
        }
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        if (gameServer == null || color == null) {
            closeWithError(session, "GameServer not properly initialized");
            return;
        }

        // Handle Message
        Message messageObj;
        try {
            messageObj = MessageDeserializer.deserialize(message);
        } catch (ProtocolException | JsonSyntaxException e) {
            closeWithError(session, "Invalid message format: " + message);
            return;
        } catch (Exception e) {
            System.err.println("Game Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
            return;
        }

        // Accept Draw Message
        if (messageObj instanceof AcceptDrawMessage acceptDrawMessage) {
            return;
        }

        // Decline Draw Message
        if (messageObj instanceof DeclineDrawMessage declineDrawMessage) {
            return;
        }

        // Move Message
        if (messageObj instanceof MoveMessage moveMessage) {
            if (gameServer.getTurn() != color) {
                sendIllegalMoveError("Not your turn");
                return;
            }
            if (!gameServer.isMoveLegal(moveMessage.getMove())) {
                sendIllegalMoveError("Invalid move");
                return;
            }
            gameServer.makeMove(moveMessage.getMove());
            return;
        }

        // Offer Draw Message
        if (messageObj instanceof OfferDrawMessage offerDrawMessage) {
            return;
        }

        // Resign Message
        if (messageObj instanceof ResignMessage) {
            gameServer.handlePlayerDisconnect(color);
            session.close();
            return;
        }

        // Client Info Message
        if (messageObj instanceof ClientInfoMessage clientInfoMessage) {
            gameServer.setClientInfo(color, clientInfoMessage.getUsername(), clientInfoMessage.getAvatar());
            return;
        }

        System.out.println("Message type not implemented yet: " + messageObj.getType());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Left GameServer: " + session.getId());
        if (gameServer != null) {
            gameServer.handlePlayerDisconnect(color);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Game Error: " + throwable.getMessage() + "\n" + throwable.getLocalizedMessage());
        if (gameServer != null) {
            closeWithError(session, throwable.getMessage());
        }
    }

    private void sendIllegalMoveError(String reason) {
        System.out.println("Illegal move: " + reason);
        IllegalMoveMessage message = new IllegalMoveMessage(reason);
        sendMessage(message);
    }

    private void sendMessage(Message message) {
        try {
            session.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        } catch (Exception e) {
            System.err.println("Game Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
        }
    }

    private void closeWithError(Session session, String message) {
        System.err.println("Closing game session due to error: " + message);
        try {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.VIOLATED_POLICY,
                    message
            ));
        } catch (IOException e) {
            System.err.println("Game Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
        }
    }
}

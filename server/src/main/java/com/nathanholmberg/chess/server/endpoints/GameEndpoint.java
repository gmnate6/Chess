package com.nathanholmberg.chess.server.endpoints;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.protocol.MessageSerializer;
import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.client.*;
import com.nathanholmberg.chess.server.models.GameServer;
import com.nathanholmberg.chess.server.models.GameManager;
import com.nathanholmberg.chess.server.models.Player;

import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;

@ServerEndpoint(WebSocketEndpoints.GAME)
public class GameEndpoint {
    private static final GameManager gameManager = GameManager.getInstance();
    private GameServer gameServer;
    private Color color;

    @OnOpen
    public void onOpen(Session session, @PathParam("gameId") String gameId, @PathParam("color") String color) {
        System.out.println("Joined GameServer: " + session.getId());

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
            gameServer.addPlayer(new Player(session), this.color);
        } catch (IllegalStateException e) {
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
            messageObj = MessageSerializer.deserialize(message);
        } catch (Exception e) {
            closeWithError(session, "Invalid message format: " + message);
            return;
        }

        // Accept Draw Message
        if (messageObj instanceof AcceptDrawMessage) {
            return;
        }

        // Decline Draw Message
        if (messageObj instanceof DeclineDrawMessage) {
            return;
        }

        // Offer Draw Message
        if (messageObj instanceof OfferDrawMessage) {
            return;
        }

        // Resign Message
        if (messageObj instanceof ResignMessage) {
            gameServer.resignPlayer(color);
            session.close();
            return;
        }

        // Client Info Message
        if (messageObj instanceof ClientInfoMessage clientInfoMessage) {
            gameServer.setClientInfo(color, clientInfoMessage.getUsername(), clientInfoMessage.getAvatar());
            return;
        }

        // Move Message
        if (messageObj instanceof MoveMessage moveMessage) {
            gameServer.makeMove(moveMessage.getMove());
            return;
        }

        // Request Game State Message
        if (messageObj instanceof RequestGameStateMessage) {
            gameServer.sendGameState(color);
        }

        System.err.println("Message type not implemented yet: " + messageObj.getType());
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        System.out.println("Left GameServer: " + session.getId() + " - " + reason.getReasonPhrase());
        if (gameServer != null) {
            gameServer.resignPlayer(color);
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Game Error: " + throwable.getMessage() + "\n" + throwable.getLocalizedMessage());
        if (gameServer != null) {
            closeWithError(session, throwable.getMessage());
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
            System.err.println("Close With Error Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
        }
    }
}

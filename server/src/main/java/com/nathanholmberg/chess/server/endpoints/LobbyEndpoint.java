package com.nathanholmberg.chess.server.endpoints;

import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;
import com.nathanholmberg.chess.protocol.messages.lobby.server.JoinedMatchmakingMessage;
import com.nathanholmberg.chess.server.models.LobbyManager;
import com.nathanholmberg.chess.protocol.MessageSerializer;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(WebSocketEndpoints.LOBBY)
public class LobbyEndpoint {
    private static final LobbyManager lobbyManager = LobbyManager.getInstance();

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("Joined Lobby: " + session.getId());
        lobbyManager.addToQueue(session);

        JoinedMatchmakingMessage message = new JoinedMatchmakingMessage();
        session.getAsyncRemote().sendText(MessageSerializer.serialize(message));
    }

    @OnClose
    public void onClose(Session session) {
        lobbyManager.removeFromQueue(session);
        System.out.println("Left Lobby: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Lobby Error from " + session.getId() +  ": " + throwable.getMessage() + "\n" + throwable.getLocalizedMessage());
    }
}

package com.nathanholmberg.chess.server.endpoints;

import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;
import com.nathanholmberg.chess.protocol.messages.lobby.server.JoinedMatchmakingMessage;
import com.nathanholmberg.chess.server.models.LobbyManager;
import com.nathanholmberg.chess.protocol.serialization.MessageSerializer;

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

    @OnMessage
    public String onMessage(String message, Session session) {
        return "Do not message lobby endpoint.";
    }

    @OnClose
    public void onClose(Session session) {
        lobbyManager.removeFromQueue(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}

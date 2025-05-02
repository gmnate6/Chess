package com.nathanholmberg.chess.server.endpoints;

import com.nathanholmberg.chess.protocol.messages.server.JoinedMatchmakingMessage;
import com.nathanholmberg.chess.server.models.LobbyManager;
import com.nathanholmberg.chess.protocol.serialization.MessageSerializer;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.Session;

@ServerEndpoint("/lobby")
public class LobbyEndpoint {
    private static final LobbyManager lobbyManager = LobbyManager.getInstance();

    @OnOpen
    public void onOpen(Session session) {
        lobbyManager.addToQueue(session);

        JoinedMatchmakingMessage message = new JoinedMatchmakingMessage();
        session.getAsyncRemote().sendText(MessageSerializer.serialize(message));
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

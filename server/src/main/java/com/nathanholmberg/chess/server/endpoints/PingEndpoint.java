package com.nathanholmberg.chess.server.endpoints;

import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;

@ServerEndpoint(WebSocketEndpoints.PING)
public class PingEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("PING: " + session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        return "pong";
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}

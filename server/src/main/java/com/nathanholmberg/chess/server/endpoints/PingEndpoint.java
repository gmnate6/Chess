package com.nathanholmberg.chess.server.endpoints;

import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.OnOpen;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.Session;

@ServerEndpoint("/ping")
public class PingEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("New connection: " + session.getId());
    }

    @OnMessage
    public String onMessage(String message, Session session) {
        // simply echo back “pong”
        System.out.println("Ping received: " + message);
        return "pong";
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("Closed connection: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
}

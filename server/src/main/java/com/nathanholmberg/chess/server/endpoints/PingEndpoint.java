package com.nathanholmberg.chess.server.endpoints;

import com.nathanholmberg.chess.protocol.constants.WebSocketEndpoints;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;

@ServerEndpoint(WebSocketEndpoints.PING)
public class PingEndpoint {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("PING: " + session.getId());
        try {
            session.close(new CloseReason(
                    CloseReason.CloseCodes.NORMAL_CLOSURE,
                    "Ping Pong"
            ));
        } catch (Exception e) {
            System.err.println("Ping Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
        }
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Ping Error from " + session.getId() +  ": " + throwable.getMessage() + "\n" + throwable.getLocalizedMessage());
    }
}

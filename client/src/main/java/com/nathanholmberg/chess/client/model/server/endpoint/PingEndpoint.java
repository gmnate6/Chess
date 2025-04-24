package com.nathanholmberg.chess.client.model.server.endpoint;

import com.nathanholmberg.chess.client.model.SettingsManager;
import jakarta.websocket.*;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@ClientEndpoint
public class PingEndpoint {
    private static final CountDownLatch latch = new CountDownLatch(1);
    private static boolean isLive;

    @OnOpen
    public void onOpen(Session session) {
        isLive = true;
        try {
            session.close();
        } catch (Exception ignored) {}
        latch.countDown();
    }

    @OnError
    public void onError(Session session, Throwable t) {
        isLive = false;
        latch.countDown();
    }

    public static boolean ping() {
        isLive = false;
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(PingEndpoint.class, URI.create(SettingsManager.getServerURL() + "/ping"));
            latch.await(5, TimeUnit.SECONDS);
        } catch (Exception ignored) {}
        return isLive;
    }

    public static void main(String[] args) {
        if (ping()) {
            System.out.println("Server is UP! 🎉");
        } else {
            System.out.println("Server is DOWN or unreachable. 💀");
        }
    }
}
package com.nathanholmberg.chess.client.model.websocket;

import com.nathanholmberg.chess.client.model.SettingsManager;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.serialization.MessageSerializer;
import jakarta.websocket.*;
import java.net.URI;

public abstract class WebSocketManager {
    private Session session;
    private final URI endpointURI;

    public WebSocketManager(String endpointPath) {
        this.endpointURI = URI.create(SettingsManager.getServerURL() + endpointPath);
    }

    public void connect() {
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(new Endpoint() {
                @Override
                public void onOpen(Session session, EndpointConfig config) {
                    WebSocketManager.this.session = session;
                    session.addMessageHandler(String.class, WebSocketManager.this::onMessageReceived);
                    onConnected();
                }

                @Override
                public void onError(Session session, Throwable thr) {
                    WebSocketManager.this.onError(thr);
                }

                @Override
                public void onClose(Session session, CloseReason closeReason) {
                    onDisconnected(closeReason);
                }
            }, endpointURI);
        } catch (Exception e) {
            onError(e);
        }
    }

    public void sendMessage(String message) {
        if (session == null || !session.isOpen()) {
            throw new IllegalStateException("WebSocket session is not initialized.");
        }

        session.getAsyncRemote().sendText(message);
    }
    public void sendMessage(Message message) {
        sendMessage(MessageSerializer.serialize(message));
    }

    public void close() {
        if (session != null && session.isOpen()) {
            try {
                session.close();
            } catch (Exception e) {
                onError(e);
            }
        }
    }

    public boolean isConnected() {
        return session != null && session.isOpen();
    }

    protected abstract void onConnected();

    protected abstract void onMessageReceived(String message);

    protected abstract void onDisconnected(CloseReason reason);

    protected abstract void onError(Throwable throwable);
}
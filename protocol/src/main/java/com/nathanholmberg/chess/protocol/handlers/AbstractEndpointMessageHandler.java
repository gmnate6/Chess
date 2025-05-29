package com.nathanholmberg.chess.protocol.handlers;

import com.google.gson.JsonSyntaxException;
import com.nathanholmberg.chess.protocol.MessageSerializer;
import com.nathanholmberg.chess.protocol.constants.Side;
import com.nathanholmberg.chess.protocol.exceptions.ProtocolException;
import com.nathanholmberg.chess.protocol.messages.Message;

public abstract class AbstractEndpointMessageHandler {
    protected final Side side;
    protected boolean ready = false;

    protected Handler handler;
    public interface Handler {
        void unknownMessage(String message, String reason);
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
        ready = true;
    }

    public AbstractEndpointMessageHandler(Side side) {
        this.side = side;
    }

    public void handleMessage(String message) {
        if (!ready) {
            throw new IllegalStateException("Handler needs to be set before messages can be handled.");
        }

        Message messageObj;
        try {
            messageObj = MessageSerializer.deserialize(message);
        } catch (ProtocolException e) {
            handler.unknownMessage(message, e.getMessage());
            return;
        } catch (JsonSyntaxException e) {
            unknownMessage(message, "Invalid JSON: " + e.getMessage());
            return;
        }

        if (side == Side.CLIENT) {
            handleClientMessage(messageObj);
        } else {
            handleServerMessage(messageObj);
        }
    }

    protected abstract void handleClientMessage(Message message);
    protected abstract void handleServerMessage(Message message);

    protected void unknownMessage(String message, String reason) {
        handler.unknownMessage(message, reason);
    }
}

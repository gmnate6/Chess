package com.nathanholmberg.chess.protocol.messages;

import com.nathanholmberg.chess.protocol.MessageRegistry;

public abstract class Message {
    private final String type = getClass().getSimpleName();

    public String getType() {
        return type;
    }

    public static void registerMessageType(Class<? extends Message> clazz) {
        MessageRegistry.register(clazz.getSimpleName(), clazz);
    }
}

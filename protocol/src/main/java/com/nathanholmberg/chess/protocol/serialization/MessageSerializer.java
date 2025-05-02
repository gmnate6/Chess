package com.nathanholmberg.chess.protocol.serialization;

import com.nathanholmberg.chess.protocol.messages.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageSerializer {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    public static String serialize(Message message) {
        return gson.toJson(message);
    }
}

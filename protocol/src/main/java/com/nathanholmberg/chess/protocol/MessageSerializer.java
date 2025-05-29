package com.nathanholmberg.chess.protocol;

import com.google.gson.*;
import com.nathanholmberg.chess.protocol.exceptions.ProtocolException;
import com.nathanholmberg.chess.protocol.messages.Message;

public class MessageSerializer {
    private static final Gson gson = new GsonBuilder()
            .serializeNulls()
            .create();

    public static String serialize(Message message) {
        return gson.toJson(message);
    }

    public static Message deserialize(String json) throws ProtocolException, JsonSyntaxException {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        Class<?extends Message> clazz = MessageRegistry.get(type);
        if (clazz == null) {
            throw new ProtocolException("Message type not in registry: " + type);
        }

        return gson.fromJson(json, clazz);
    }
}

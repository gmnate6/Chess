package com.nathanholmberg.chess.protocol.serialization;

import com.nathanholmberg.chess.protocol.exceptions.ProtocolException;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.client.*;
import com.nathanholmberg.chess.protocol.messages.server.*;

import com.google.gson.*;

public class MessageDeserializer {
    private static final Gson gson = new GsonBuilder()
            .create();

    public static Message deserialize(String json) throws ProtocolException, JsonSyntaxException {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        String type = jsonObject.get("type").getAsString();

        // Based on the type string, determine which class to deserialize to
        return switch (type) {
            // Client
            case "AcceptDrawMessage"        -> gson.fromJson(json, AcceptDrawMessage.class);
            case "DeclineDrawMessage"       -> gson.fromJson(json, DeclineDrawMessage.class);
            case "MoveMessage"              -> gson.fromJson(json, MoveMessage.class);
            case "OfferDrawMessage"         -> gson.fromJson(json, OfferDrawMessage.class);
            case "ResignMessage"            -> gson.fromJson(json, ResignMessage.class);

            // Server
            case "ClockUpdateMessage"       -> gson.fromJson(json, ClockUpdateMessage.class);
            case "DrawOfferedMessage"       -> gson.fromJson(json, DrawOfferedMessage.class);
            case "GameStateMessage"         -> gson.fromJson(json, GameStateMessage.class);
            case "GameStartMessage"         -> gson.fromJson(json, GameStartMessage.class);
            case "IllegalMoveMessage"       -> gson.fromJson(json, IllegalMoveMessage.class);
            case "GameReadyMessage"        -> gson.fromJson(json, GameReadyMessage.class);
            case "JoinedMatchmakingMessage" -> gson.fromJson(json, JoinedMatchmakingMessage.class);
            case "MoveAcceptedMessage"      -> gson.fromJson(json, MoveAcceptedMessage.class);

            default -> throw new ProtocolException("Unknown message type: " + type);
        };
    }
}

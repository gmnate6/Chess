package com.nathanholmberg.chess.protocol;

import com.google.gson.*;
import com.nathanholmberg.chess.protocol.exceptions.ProtocolException;
import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.client.*;
import com.nathanholmberg.chess.protocol.messages.game.server.*;
import com.nathanholmberg.chess.protocol.messages.lobby.server.GameReadyMessage;
import com.nathanholmberg.chess.protocol.messages.lobby.server.JoinedMatchmakingMessage;

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

        // Based on the type string, determine which class to deserialize to
        return switch (type) {
            // Lobby (Server)
            case "GameReadyMessage"         -> gson.fromJson(json, GameReadyMessage.class);
            case "JoinedMatchmakingMessage" -> gson.fromJson(json, JoinedMatchmakingMessage.class);

            // Game
            case "ClientInfoMessage"        -> gson.fromJson(json, ClientInfoMessage.class);
            case "MoveMessage"              -> gson.fromJson(json, MoveMessage.class);

            // Game (Client)
            case "AcceptDrawMessage"        -> gson.fromJson(json, AcceptDrawMessage.class);
            case "DeclineDrawMessage"       -> gson.fromJson(json, DeclineDrawMessage.class);
            case "OfferDrawMessage"         -> gson.fromJson(json, OfferDrawMessage.class);
            case "RequestGameStateMessage"  -> gson.fromJson(json, RequestGameStateMessage.class);
            case "ResignMessage"            -> gson.fromJson(json, ResignMessage.class);

            // Game (Server)
            case "ClockUpdateMessage"       -> gson.fromJson(json, ClockUpdateMessage.class);
            case "DrawOfferedMessage"       -> gson.fromJson(json, DrawOfferedMessage.class);
            case "GameEndMessage"           -> gson.fromJson(json, GameEndMessage.class);
            case "GameStartMessage"         -> gson.fromJson(json, GameStartMessage.class);
            case "GameStateMessage"         -> gson.fromJson(json, GameStateMessage.class);
            case "IllegalMoveMessage"       -> gson.fromJson(json, IllegalMoveMessage.class);

            default -> throw new ProtocolException("Unknown message type: " + type);
        };
    }
}

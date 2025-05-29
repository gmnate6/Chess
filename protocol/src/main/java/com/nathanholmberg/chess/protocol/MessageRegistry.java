package com.nathanholmberg.chess.protocol;

import com.nathanholmberg.chess.protocol.messages.Message;
import com.nathanholmberg.chess.protocol.messages.game.ClientInfoMessage;
import com.nathanholmberg.chess.protocol.messages.game.MoveMessage;
import com.nathanholmberg.chess.protocol.messages.game.client.*;
import com.nathanholmberg.chess.protocol.messages.game.server.*;
import com.nathanholmberg.chess.protocol.messages.lobby.server.GameReadyMessage;
import com.nathanholmberg.chess.protocol.messages.lobby.server.JoinedMatchmakingMessage;

import java.util.HashMap;
import java.util.Map;

public class MessageRegistry {
    private static final Map<String, Class<? extends Message>> registry = new HashMap<>();

    public static void register(String type, Class<? extends Message> clazz) {
        registry.put(type, clazz);
    }

    public static Class<?extends Message> get(String type) {
        return registry.get(type);
    }

    public static void init() {
        // Lobby (Server)
        Message.registerMessageType(GameReadyMessage.class);
        Message.registerMessageType(JoinedMatchmakingMessage.class);

        // Game
        Message.registerMessageType(ClientInfoMessage.class);
        Message.registerMessageType(MoveMessage.class);

        // Game (Client)
        Message.registerMessageType(AcceptDrawMessage.class);
        Message.registerMessageType(DeclineDrawMessage.class);
        Message.registerMessageType(OfferDrawMessage.class);
        Message.registerMessageType(RequestGameStateMessage.class);
        Message.registerMessageType(ResignMessage.class);

        // Game (Server)
        Message.registerMessageType(ClockUpdateMessage.class);
        Message.registerMessageType(DrawOfferedMessage.class);
        Message.registerMessageType(GameEndMessage.class);
        Message.registerMessageType(GameStartMessage.class);
        Message.registerMessageType(GameStateMessage.class);
        Message.registerMessageType(IllegalMoveMessage.class);
    }
}
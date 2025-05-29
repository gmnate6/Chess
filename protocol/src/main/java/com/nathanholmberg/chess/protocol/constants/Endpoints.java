package com.nathanholmberg.chess.protocol.constants;

public class Endpoints {
    public static final String GAME  = "/game/{gameId}/{color}";
    public static final String LOBBY = "/lobby";
    public static final String PING  = "/ping";

    private Endpoints() {}
}

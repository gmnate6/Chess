package com.nathanholmberg.chess.server.models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    private static final GameManager instance = new GameManager();
    private final Map<String, GameServer> activeGames = new ConcurrentHashMap<>();

    private GameManager() {}
    public static GameManager getInstance() {
        return instance;
    }

    public void addGame(String gameId, GameServer gameServer) {
        activeGames.put(gameId, gameServer);
    }

    public GameServer getGame(String gameId) {
        return activeGames.get(gameId);
    }

    public void removeGame(String gameId) {
        activeGames.remove(gameId);
    }
}

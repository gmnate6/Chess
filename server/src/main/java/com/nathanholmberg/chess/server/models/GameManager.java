package com.nathanholmberg.chess.server.models;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    private static final GameManager instance = new GameManager();
    private final Map<String, ServerGame> activeGames = new ConcurrentHashMap<>();

    private GameManager() {}
    public static GameManager getInstance() {
        return instance;
    }

    public void addGame(String gameId, ServerGame serverGame) {
        activeGames.put(gameId, serverGame);
    }

    public ServerGame getGame(String gameId) {
        return activeGames.get(gameId);
    }

    public void removeGame(String gameId) {
        activeGames.remove(gameId);
    }
}

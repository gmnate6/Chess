package com.nathanholmberg.chess.server.models;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.protocol.messages.lobby.server.GameReadyMessage;
import com.nathanholmberg.chess.protocol.serialization.MessageSerializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.websocket.CloseReason;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public class LobbyManager {
    private static final LobbyManager instance = new LobbyManager();
    private final Queue<Session> playerQueue = new ConcurrentLinkedQueue<>();
    private final GameManager gameManager = GameManager.getInstance();
    private static final Gson GSON = new GsonBuilder().create();

    private LobbyManager() {
        startMatchmakingThread();
    }

    public static synchronized LobbyManager getInstance() {
        return instance;
    }

    public void addToQueue(Session session) {
        playerQueue.offer(session);
    }

    public void removeFromQueue(Session session) {
        playerQueue.remove(session);
    }

    private void startMatchmakingThread() {
        Thread matchmakingThread = new Thread(() -> {
            while (true) {
                try {
                    matchPlayers();
                    Thread.sleep(1000); // Check for matches every second
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        matchmakingThread.setDaemon(true);
        matchmakingThread.start();

    }

    private void matchPlayers() {
        while (getQueueSize() >= 2) {
            Session player1 = playerQueue.poll();
            Session player2 = playerQueue.poll();

            // If player left
            boolean player1Left = player1 == null || !player1.isOpen();
            boolean player2Left = player2 == null || !player2.isOpen();
            if (player1Left && player2Left) {
                return;
            }
            if (player1Left) {
                playerQueue.offer(player2);
                return;
            }
            if (player2Left) {
                playerQueue.offer(player1);
                return;
            }

            try {
                createGame(player1, player2);
            } catch (Exception e) {
                e.printStackTrace();
                playerQueue.offer(player1);
                playerQueue.offer(player2);
            }
        }
    }

    private void createGame(Session player1, Session player2) {
        System.out.println("Creating serverGame between " + player1.getId() + " and " + player2.getId());
        // Randomly assign colors
        boolean player1IsWhite = Math.random() < 0.5;
        Session whitePlayer = player1IsWhite ? player1 : player2;
        Session blackPlayer = player1IsWhite ? player2 : player1;

        // Create serverGame
        String gameId = UUID.randomUUID().toString();

        // Create and store serverGame instance
        ServerGame serverGame = new ServerGame(gameId);
        gameManager.addGame(gameId, serverGame);

        // Notify players
        GameReadyMessage message;
        message = new GameReadyMessage(gameId, Color.WHITE.toString());
        whitePlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        message = new GameReadyMessage(gameId, Color.BLACK.toString());
        blackPlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
    }

    public int getQueueSize() {
        return playerQueue.size();
    }

    public void clearQueue() {
        for (Session session : playerQueue) {
            try {
                session.close(new CloseReason(
                        CloseReason.CloseCodes.SERVICE_RESTART,
                        "Server shutting down"
                ));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        playerQueue.clear();
    }
}

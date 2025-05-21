package com.nathanholmberg.chess.server.models;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.protocol.messages.lobby.server.GameReadyMessage;
import com.nathanholmberg.chess.protocol.MessageSerializer;

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
                    System.err.println("LobbyManager Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
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

            // Create game
            try {
                createGame(player1, player2);
            } catch (Exception e) {
                System.err.println("LobbyManager Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
                playerQueue.offer(player1);
                playerQueue.offer(player2);
            }
        }
    }

    private void createGame(Session player1, Session player2) {
        // Randomly assign colors
        boolean player1IsWhite = Math.random() < 0.5;
        Session whitePlayer = player1IsWhite ? player1 : player2;
        Session blackPlayer = player1IsWhite ? player2 : player1;

        // Create GameServer
        String gameId = UUID.randomUUID().toString();
        GameServer gameServer = new GameServer(gameId);
        gameManager.addGame(gameId, gameServer);

        // Notify players
        GameReadyMessage message;
        message = new GameReadyMessage(gameId, Color.WHITE);
        whitePlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));
        message = new GameReadyMessage(gameId, Color.BLACK);
        blackPlayer.getAsyncRemote().sendText(MessageSerializer.serialize(message));

        // Close the sessions
        try {
            player1.close(new CloseReason(
                    CloseReason.CloseCodes.NORMAL_CLOSURE,
                    "Game started"
            ));
            player2.close(new CloseReason(
                    CloseReason.CloseCodes.NORMAL_CLOSURE,
                    "Game started"
            ));
        } catch (IOException e) {
            System.err.println("LobbyManager Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
        }
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
                System.err.println("LobbyManager Error: " + e.getMessage() + "\n" + e.getLocalizedMessage());
            }
        }
        playerQueue.clear();
    }
}

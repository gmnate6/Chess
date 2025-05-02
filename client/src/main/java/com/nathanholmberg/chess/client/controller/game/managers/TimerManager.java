package com.nathanholmberg.chess.client.controller.game.managers;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.game.GamePanel;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessGame;

import javax.swing.*;

public class TimerManager {
    private final ChessGame chessGame;
    private final GamePanel gamePanel;
    private final Color color;
    private Timer swingTimer;

    public TimerManager(ChessGame chessGame, GamePanel gamePanel, Color color) {
        this.chessGame = chessGame;
        this.gamePanel = gamePanel;
        this.color = color;
        initTimer();
    }

    private void initTimer() {
        swingTimer = new Timer(200, e -> {
            if (chessGame.getTimer() == null)
                return;

            // 10 Seconds Left Warning
            if (chessGame.getTimer().getTimeLeft(color) <= 10_000) {
                AssetManager.playSound("ten-seconds");
            }

            gamePanel.setTopTimer(chessGame.getTimer().getFormatedTimeLeft(color.inverse()));
            gamePanel.setBottomTimer(chessGame.getTimer().getFormatedTimeLeft(color));
        });
    }

    public void start() {
        swingTimer.start();
    }

    public void stop() {
        swingTimer.stop();
    }
}

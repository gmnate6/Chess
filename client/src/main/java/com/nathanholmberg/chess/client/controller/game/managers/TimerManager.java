package com.nathanholmberg.chess.client.controller.game.managers;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.game.GamePanel;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessTimer;

import javax.swing.*;

public class TimerManager {
    private final ChessTimer chessTimer;
    private final GamePanel gamePanel;
    private final Color color;
    private Timer swingTimer;

    public TimerManager(ChessTimer chessTimer, GamePanel gamePanel, Color color) {
        this.chessTimer = chessTimer;
        this.gamePanel = gamePanel;
        this.color = color;
        initTimer();
    }

    private void initTimer() {
        swingTimer = new Timer(200, e -> {
            if (chessTimer == null)
                return;

            // 10 Seconds Left Warning
            if (chessTimer.getTimeLeft(color) <= 10_000) {
                AssetManager.playSound("ten-seconds");
            }

            gamePanel.setTopTimer(chessTimer.getFormatedTimeLeft(color.inverse()));
            gamePanel.setBottomTimer(chessTimer.getFormatedTimeLeft(color));
        });
    }

    public void start() {
        swingTimer.start();
    }

    public void stop() {
        swingTimer.stop();
    }
}

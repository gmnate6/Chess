package frontend.controller.game.managers;

import engine.game.Game;
import frontend.model.assets.AssetManager;
import frontend.view.game.GamePanel;
import utils.Color;

import javax.swing.*;

public class TimerManager {
    private final Game game;
    private final GamePanel gamePanel;
    private final Color color;
    private Timer swingTimer;

    public TimerManager(Game game, GamePanel gamePanel, Color color) {
        this.game = game;
        this.gamePanel = gamePanel;
        this.color = color;
        initTimer();
    }

    private void initTimer() {
        swingTimer = new Timer(200, e -> {
            if (game.getTimer() == null)
                return;

            // 10 Seconds Left Warning
            if (game.getTimer().getTimeLeft(color) <= 10_000) {
                AssetManager.playSound("ten-seconds");
            }

            gamePanel.setTopTimer(game.getTimer().getFormatedTimeLeft(color.inverse()));
            gamePanel.setBottomTimer(game.getTimer().getFormatedTimeLeft(color));
        });
    }

    public void start() {
        swingTimer.start();
    }

    public void stop() {
        swingTimer.stop();
    }
}

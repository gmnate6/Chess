package com.nathanholmberg.chess.client.controller.game;

import com.nathanholmberg.chess.client.controller.MainController;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.types.Move;

public class SoloGameController extends AbstractGameController {
    public SoloGameController() {
        super(Color.WHITE);
        start();
        configureSoloGameSettings();
    }

    private void configureSoloGameSettings() {
        gamePanel.topBannerPanel.setVisible(false);
        gamePanel.bottomBannerPanel.setVisible(false);
        gamePanel.drawButton.setEnabled(false);
    }

    @Override
    protected void rematch() {
        super.rematch();
        MainController.switchTo(new SoloGameController());
    }

    public void switchSides() {
        setColor(color.inverse());
        setPerspective(color);
    }

    @Override
    public void executeMove(Move move) {
        super.executeMove(move);
        switchSides();
    }
}

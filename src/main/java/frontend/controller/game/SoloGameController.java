package frontend.controller.game;

import engine.types.Move;
import utils.Color;

public class SoloGameController extends AbstractGameController {
    public SoloGameController() {
        super();
        color = Color.WHITE;
        startGame(color, null);
        setPerspective(color);

        // Hide Banner Panels and Draw and Buttons Panel
        gamePanel.topBannerPanel.setVisible(false);
        gamePanel.bottomBannerPanel.setVisible(false);
        gamePanel.showBackButton();
    }

    public void switchSides() {
        color = color.inverse();
        setPerspective(color);
    }

    @Override
    public void executeMove(Move move) {
        super.executeMove(move);
        switchSides();
    }
}

package frontend.controller.game;

import engine.types.Move;
import frontend.view.game.GamePanel;
import utils.Color;

public class SoloGameController extends AbstractGameController {
    public SoloGameController(GamePanel gamePanel) {
        super(gamePanel);
        color = Color.WHITE;
        startGame(color, null);
        setPerspective(color);

        // Hide Banner Panels and Draw and Buttons Panel
        gamePanel.topBannerPanel.setVisible(false);
        gamePanel.bottomBannerPanel.setVisible(false);
        gamePanel.drawButton.setEnabled(false);
    }

    public void switchSides() {
        color = color.inverse();
    }

    @Override
    public void executeMove(Move move) {
        super.executeMove(move);
        switchSides();
    }
}

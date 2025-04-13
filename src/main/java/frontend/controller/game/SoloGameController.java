package frontend.controller.game;

import engine.types.Move;
import frontend.controller.MainController;
import utils.Color;

public class SoloGameController extends AbstractGameController {
    public SoloGameController() {
        super();
        color = Color.WHITE;
        startGame(color, null);
        setPerspective(color);

        // Disable Draw
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
        color = color.inverse();
        setPerspective(color);
    }

    @Override
    public void executeMove(Move move) {
        super.executeMove(move);
        switchSides();
    }
}

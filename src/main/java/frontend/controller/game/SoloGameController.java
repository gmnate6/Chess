package frontend.controller.game;

import engine.types.Move;
import frontend.controller.MainController;
import utils.Color;

public class SoloGameController extends AbstractGameController {
    public SoloGameController() {
        super(Color.WHITE, null);
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

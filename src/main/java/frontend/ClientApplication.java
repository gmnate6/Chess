package frontend;

import frontend.controller.GameController;
import frontend.model.GameModel;
import frontend.view.game.GamePanel;

import utils.Color;

public class ClientApplication {
    GameModel gameModel;
    GamePanel gamePanel;
    GameController gameController;

    public ClientApplication(Color color) {
        gamePanel = new GamePanel();
        gameModel = new GameModel();
        gameController = new GameController(gamePanel, null);

        gameController.startGame(color, null);
        gamePanel.createJFrame();
    }

    public static void main(String[] args) {
        new ClientApplication(Color.WHITE);
    }
}

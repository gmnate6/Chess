package frontend;

import frontend.controller.GameController;
import frontend.model.server.GameServerManager;
import frontend.view.game.GamePanel;

import utils.Color;

public class ClientApplication {
    GameServerManager gameServerManager;
    GamePanel gamePanel;
    GameController gameController;

    public ClientApplication(Color color) {
        gamePanel = new GamePanel();
        gameServerManager = new GameServerManager();
        gameController = new GameController(gamePanel, null);

        gameController.startGame(color, null);
        gamePanel.createJFrame();
    }

    public static void main(String[] args) {
        new ClientApplication(Color.WHITE);
    }
}

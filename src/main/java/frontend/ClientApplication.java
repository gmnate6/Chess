package frontend;

import frontend.model.GameModel;
import frontend.view.game.BoardPanel;
import frontend.controller.GameController;

import utils.Color;

public class ClientApplication {
    GameModel gameModel;
    BoardPanel boardPanel;
    GameController gameController;

    public ClientApplication() {
        boardPanel = new BoardPanel();
        gameModel = new GameModel();
        gameController = new GameController(boardPanel, gameModel);

        gameController.startGame(Color.WHITE, 600_000, 0);
        boardPanel.createJFrame();
    }

    public static void main(String[] args) {
        new ClientApplication();
    }
}

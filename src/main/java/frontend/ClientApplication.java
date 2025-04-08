package frontend;

import engine.ai.RandomAI;
import engine.ai.StockfishAI;
import frontend.controller.game.AbstractGameController;
import frontend.controller.game.LocalBotGameController;
import frontend.controller.game.SoloGameController;
import frontend.view.game.GamePanel;
import utils.Color;

import javax.swing.*;

public class ClientApplication {
    GamePanel gamePanel;
    AbstractGameController gameController;

    public AbstractGameController getGameController() {
        // Ask user what mode
        String[] modeOptions = { "Solo Game", "Play Stockfish AI", "Play Random AI" };
        int modeChoice = JOptionPane.showOptionDialog(null,
                "Choose Game Mode:",
                "Game Mode Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                modeOptions,
                modeOptions[0]);

        // Solo
        if (modeChoice == 0) {
            return new SoloGameController(gamePanel);
        }

        // Ask user what color
        String[] colorOptions = { "White", "Black" };
        int colorChoice = JOptionPane.showOptionDialog(null,
                "Choose Color:",
                "Color Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                colorOptions,
                colorOptions[0]);
        Color color = colorChoice == 0 ? Color.WHITE : Color.BLACK;

        // Handle the user's selection
        switch (modeChoice) {
            case 1:
                // Stockfish AI
                return new LocalBotGameController(gamePanel, color, new StockfishAI());
            case 2:
                // Random AI
                return new LocalBotGameController(gamePanel, color, new RandomAI());
        }

        // If the user closes the dialog or selects nothing
        JOptionPane.showMessageDialog(null, "No selection made. Exiting game.");
        System.exit(0);
        return null;
    }

    public ClientApplication() {
        gamePanel = new GamePanel();
        gameController = getGameController();
        gamePanel.createJFrame();
    }

    public static void main(String[] args) {
        new ClientApplication();
    }
}

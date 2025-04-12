package frontend;

import engine.ai.RandomAI;
import engine.ai.StockfishAI;
import frontend.controller.game.AbstractGameController;
import frontend.controller.game.BotGameController;
import frontend.controller.game.SoloGameController;
import utils.Color;

import javax.swing.*;

public class ClientApplicationOLD {
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

        // Early Return
        if (modeChoice == -1) {
            System.exit(0);
        }

        // Solo
        if (modeChoice == 0) {
            return new SoloGameController();
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
                return new BotGameController(color, new StockfishAI());
            case 2:
                // Random AI
                return new BotGameController(color, new RandomAI());
        }

        // If the user closes the dialog or selects nothing
        System.exit(0);
        return null;
    }

    public ClientApplicationOLD() {
        gameController = getGameController();
    }

    public static void main(String[] args) {
        new ClientApplicationOLD();
    }
}

package frontend;

import frontend.controller.GameController;
import frontend.model.GameModel;
import frontend.view.game.BoardPanel;

import utils.Color;

import javax.swing.*;

public class ClientApplication {
    GameModel gameModel;
    BoardPanel boardPanel;
    GameController gameController;

    public ClientApplication(Color color) {
        boardPanel = new BoardPanel();
        gameModel = new GameModel();
        gameController = new GameController(boardPanel, null);

        gameController.startGame(color, null);
        boardPanel.createJFrame();
    }

    public static void main(String[] args) {
        // Prompt the user to choose white or black using a pop-up dialog
        Object[] options = {"White", "Black", "AI vs AI"}; // Options for the dialog
        int choice = JOptionPane.showOptionDialog(
                null,
                "Which color would you like to play as?",
                "Choose Your Color",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
        );

        // Determine color based on the user's choice
        Color chosenColor;
        if (choice == JOptionPane.YES_OPTION) {
            chosenColor = Color.WHITE;
        } else if (choice == JOptionPane.NO_OPTION) {
            chosenColor = Color.BLACK;
        } else {
            // AI vs. AI
            chosenColor = null;
        }

        // Start the application with the chosen color
        new ClientApplication(chosenColor);
    }
}

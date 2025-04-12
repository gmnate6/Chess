package frontend.controller.menu;

import engine.ai.ChessAI;
import engine.ai.RandomAI;
import engine.ai.StockfishAI;
import frontend.controller.BaseController;
import frontend.controller.MainController;
import frontend.controller.game.BotGameController;
import frontend.view.components.button.CustomButton;
import frontend.view.menu.BotPanel;
import utils.Color;

import javax.swing.*;

public class BotController implements BaseController {
    private final BotPanel botPanel;

    public BotController() {
        botPanel = new BotPanel();

        // Bot Dropdown
        botPanel.botDropdown.addItem(new StockfishAI());
        botPanel.botDropdown.addItem(new RandomAI());
        botPanel.botDropdown.setSelectedIndex(0);

        // Color Dropdown
        botPanel.colorDropdown.addItem(Color.WHITE);
        botPanel.colorDropdown.addItem(Color.BLACK);
        botPanel.colorDropdown.setSelectedIndex(0);

        // Play Button Listener
        botPanel.playButton.addActionListener(
                e -> play()
        );

        // Back Button Listener
        botPanel.backButton.addActionListener(
                e -> MainController.switchTo(new TitleController())
        );
    }

    private void play() {
        Color color = (Color) botPanel.colorDropdown.getSelectedItem();
        ChessAI bot = (ChessAI) botPanel.botDropdown.getSelectedItem();
        if (bot == null || color == null) {
            JOptionPane.showMessageDialog(null, "Invalid Input");
            return;
        }
        MainController.switchTo(new BotGameController(color, bot));
    }

    @Override
    public void dispose() {
        // Loop through all components in the panel
        for (java.awt.Component component : botPanel.getComponents()) {
            // Check if the component is a button
            if (component instanceof CustomButton button) {
                button.dispose();
            }
        }
    }

    @Override
    public JPanel getPanel() {
        return botPanel;
    }
}

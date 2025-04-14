package frontend;

import frontend.controller.MainController;
import frontend.model.SettingsManager;
import frontend.model.assets.AssetManager;
import frontend.view.MainFrame;

import javax.swing.*;

public class ClientApplication {
    public static void main(String[] args) {
        // Initialize Assets & Settings
        AssetManager.initialize();
        SettingsManager.initialize();

        // Start Application
        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame();
            new MainController(mainFrame);
            mainFrame.setVisible(true);
        });
    }
}

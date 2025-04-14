package frontend;

import frontend.controller.MainController;
import frontend.model.SettingsManager;
import frontend.model.assets.AssetManager;
import frontend.view.MainFrame;
import frontend.view.utils.SplashScreen;

import javax.swing.*;

public class ClientApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Splash Screen
            SplashScreen splash = new SplashScreen();
            splash.setVisible(true);

            // Initialize off EDT -> Start App on EDT
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    // Initialize Assets & Settings
                    AssetManager.initialize();
                    SettingsManager.initialize();
                    return null;
                }

                @Override
                protected void done() {
                    // Dispose of the splash screen.
                    splash.dispose();

                    // Start application.
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setLocationRelativeTo(null);
                    new MainController(mainFrame);
                    mainFrame.setVisible(true);
                }
            }.execute();
        });
    }
}

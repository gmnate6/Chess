package com.nathanholmberg.chess.client;

import com.nathanholmberg.chess.client.controller.MainController;
import com.nathanholmberg.chess.client.model.SettingsManager;
import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.MainFrame;
import com.nathanholmberg.chess.client.view.utils.SplashScreen;

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
                    // Dispose of the splash screen
                    splash.dispose();

                    // Start application
                    MainFrame mainFrame = new MainFrame();
                    mainFrame.setLocationRelativeTo(null);
                    new MainController(mainFrame);
                    mainFrame.setVisible(true);
                }
            }.execute();
        });
    }
}

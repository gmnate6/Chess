package com.nathanholmberg.chess.client.view.utils;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.panels.DynamicImagedPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SplashScreen extends JWindow {
    public SplashScreen() {
        setBackground(new Color(0, 0, 0, 0));

        // Settings
        BufferedImage logo = AssetManager.getIcon();
        float screenPercent = 0.2f;

        // Set size
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int size =  (int) (screenSize.height * screenPercent);
        setSize(size, size);

        // Center on screen
        setLocationRelativeTo(null);

        // Also set the content pane background to transparent
        getContentPane().setBackground(new Color(0, 0, 0, 0));
        getContentPane().setLayout(new BorderLayout());

        // Add the logo
        DynamicImagedPanel logoPanel = new DynamicImagedPanel();
        logoPanel.setImage(logo);
        getContentPane().add(logoPanel, BorderLayout.CENTER);
    }
}

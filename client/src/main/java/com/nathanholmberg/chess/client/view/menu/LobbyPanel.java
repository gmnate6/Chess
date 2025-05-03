package com.nathanholmberg.chess.client.view.menu;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.button.TransparentButton;

import javax.swing.*;
import java.awt.*;

public class LobbyPanel extends AbstractMenuPanel {
    public JLabel lobbyLabel;
    public TransparentButton backButton;

    public LobbyPanel() {
        super("Lobby", 1f);
        heightRatio = .5f;
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        contentPanel.setLayout(new GridLayout(0, 1, 20, 20));

        // Lobby Label
        lobbyLabel = new JLabel("Waiting...");
        lobbyLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lobbyLabel.setForeground(AssetManager.getThemeColor("text"));
        contentPanel.add(lobbyLabel);

        // Back Button
        backButton = new TransparentButton("Back");
        contentPanel.add(backButton);
    }

    @Override
    protected void onResize() {
        super.onResize();

        // Calculate font sizes dynamically
        int baseFontSize = getBaseFontSize();

        // Update button font sizes
        lobbyLabel.setFont(AssetManager.getFont("chess_font", baseFontSize));
        backButton.setFont(AssetManager.getFont("chess_font", baseFontSize));

        // Revalidate and repaint the panel to apply changes
        revalidate();
        repaint();
    }
}
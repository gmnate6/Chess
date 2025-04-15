package com.nathanholmberg.chess.client.view.utils;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.panels.DynamicImagedPanel;
import com.nathanholmberg.chess.client.view.components.panels.TranslucentPanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ProfilePanel extends TranslucentPanel {
    public DynamicImagedPanel avatarPanel;
    public JLabel usernameLabel;

    public ProfilePanel() {
        super(new BorderLayout());
        setBorder(new EmptyBorder(5, 5, 5, 5));

        // Avatar Buffer Panel
        JPanel avatarBufferPanel = new JPanel(new SquareLayoutManager());
        avatarBufferPanel.setBorder(new EmptyBorder(0, 0, 0, 10));
        avatarBufferPanel.setOpaque(false);
        add(avatarBufferPanel, BorderLayout.WEST);

        // Avatar Panel
        avatarPanel = new DynamicImagedPanel();
        avatarPanel.setPreferredSize(new Dimension(50, 50));
        avatarPanel.setImage(AssetManager.getAvatar("default"));
        avatarBufferPanel.add(avatarPanel);

        // Username Label
        usernameLabel = new JLabel("--Username--", SwingConstants.CENTER);
        usernameLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        usernameLabel.setFont(AssetManager.getFont("chess_font", 16));
        usernameLabel.setForeground(AssetManager.getThemeColor("text"));
        add(usernameLabel, BorderLayout.CENTER);
    }

    public void setAvatar(String avatar) {
        avatarPanel.setImage(AssetManager.getAvatar(avatar));
    }
    public void setUsername(String username) {
        usernameLabel.setText(username);
    }
}

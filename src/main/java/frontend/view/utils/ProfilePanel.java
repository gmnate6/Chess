package frontend.view.utils;

import frontend.model.assets.AssetManager;
import frontend.view.components.panels.DynamicImagedPanel;
import frontend.view.components.panels.TranslucentPanel;

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
        avatarPanel.setImage(AssetManager.getInstance().getAvatar("default"));
        avatarBufferPanel.add(avatarPanel);

        // Username Label
        usernameLabel = new JLabel("--Username--", SwingConstants.CENTER);
        usernameLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        usernameLabel.setFont(AssetManager.getInstance().getFont("chess_font", 16));
        usernameLabel.setForeground(AssetManager.getInstance().getThemeColor("text"));
        add(usernameLabel, BorderLayout.CENTER);
    }

    public void setAvatar(String avatar) {
        avatarPanel.setImage(AssetManager.getInstance().getAvatar(avatar));
    }
    public void setUsername(String username) {
        usernameLabel.setText(username);
    }
}

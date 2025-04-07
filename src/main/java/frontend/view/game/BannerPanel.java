package frontend.view.game;

import frontend.model.assets.AssetManager;
import frontend.view.utils.DynamicImagedPanel;
import frontend.view.utils.SquareLayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BannerPanel extends JPanel {
    public BannerPanel() {
        /// Setup Banner Panel
        setLayout(new BorderLayout());
        setBackground(AssetManager.getInstance().getThemeColor("panel"));
        setPreferredSize(new Dimension(50, 50));

        /// Add Profile
        JPanel profilePanel = new JPanel(new BorderLayout());
        profilePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        profilePanel.setOpaque(false);

        // Avatar Buffer Panel
        JPanel avatarBufferPanel = new JPanel(new SquareLayoutManager());
        avatarBufferPanel.setOpaque(false);

        // Avatar Panel
        DynamicImagedPanel avatarPanel = new DynamicImagedPanel();
        avatarPanel.setPreferredSize(new Dimension(50, 50));
        avatarPanel.setImage(AssetManager.getInstance().getAvatar("default"));
        avatarBufferPanel.add(avatarPanel);

        // Add
        profilePanel.add(avatarBufferPanel, BorderLayout.WEST);

        // Username Label
        JLabel usernameLabel = new JLabel("Username", SwingConstants.CENTER);
        usernameLabel.setFont(AssetManager.getInstance().getFont("chess_font", 16));
        usernameLabel.setForeground(AssetManager.getInstance().getThemeColor("text"));
        profilePanel.add(usernameLabel, BorderLayout.CENTER);

        // Add
        add(profilePanel, BorderLayout.WEST);

        /// Add Timer
        JPanel timerBufferPanel = new JPanel(new BorderLayout());
        timerBufferPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        timerBufferPanel.setOpaque(false);

        JPanel timerPanel = new JPanel(new BorderLayout());
        timerPanel.setBackground(AssetManager.getInstance().getThemeColor("panel"));

        // Timer Label
        JLabel timerLabel = new JLabel("00:00", SwingConstants.CENTER);
        timerLabel.setFont(AssetManager.getInstance().getFont("chess_font", 32));
        timerLabel.setForeground(AssetManager.getInstance().getThemeColor("text"));

        // Add
        timerPanel.add(timerLabel, BorderLayout.CENTER);
        timerBufferPanel.add(timerPanel);
        add(timerBufferPanel, BorderLayout.EAST);

    }
}

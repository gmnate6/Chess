package frontend.view.game;

import frontend.model.assets.AssetManager;
import frontend.view.components.panels.TranslucentPanel;
import frontend.view.components.panels.DynamicImagedPanel;
import frontend.view.utils.SquareLayoutManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BannerPanel extends JPanel {
    public DynamicImagedPanel avatarPanel;
    public JLabel usernameLabel;
    public JLabel timerLabel;
    public JPanel timerBufferPanel;

    public BannerPanel() {
        /// Setup Banner Panel
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(50, 50));
        setOpaque(false);

        /// Add Profile
        TranslucentPanel profilePanel = new TranslucentPanel(new BorderLayout());
        profilePanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Avatar Buffer Panel
        JPanel avatarBufferPanel = new JPanel(new SquareLayoutManager());
        avatarBufferPanel.setOpaque(false);

        // Avatar Panel
        avatarPanel = new DynamicImagedPanel();
        avatarPanel.setPreferredSize(new Dimension(50, 50));
        avatarPanel.setImage(AssetManager.getInstance().getAvatar("default"));
        avatarBufferPanel.add(avatarPanel);

        // Add
        profilePanel.add(avatarBufferPanel, BorderLayout.WEST);

        // Username Label
        usernameLabel = new JLabel("--Username--", SwingConstants.CENTER);
        usernameLabel.setFont(AssetManager.getInstance().getFont("chess_font", 16));
        usernameLabel.setForeground(AssetManager.getInstance().getThemeColor("text"));
        profilePanel.add(usernameLabel, BorderLayout.CENTER);

        // Add
        add(profilePanel, BorderLayout.WEST);

        /// Add Timer
        timerBufferPanel = new JPanel(new BorderLayout());
        timerBufferPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        timerBufferPanel.setPreferredSize(new Dimension(120, 50));
        timerBufferPanel.setOpaque(false);
        add(timerBufferPanel, BorderLayout.EAST);

        TranslucentPanel timerPanel = new TranslucentPanel(new BorderLayout());
        timerBufferPanel.add(timerPanel);

        // Timer Label
        timerLabel = new JLabel("--:--", SwingConstants.CENTER);
        timerLabel.setFont(AssetManager.getInstance().getFont("chess_font", 32));
        timerLabel.setForeground(AssetManager.getInstance().getThemeColor("text"));
        timerPanel.add(timerLabel, BorderLayout.CENTER);
    }
}

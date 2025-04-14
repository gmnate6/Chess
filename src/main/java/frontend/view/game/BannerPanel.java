package frontend.view.game;

import frontend.model.assets.AssetManager;
import frontend.view.components.panels.TranslucentPanel;
import frontend.view.utils.ProfilePanel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BannerPanel extends JPanel {
    public ProfilePanel profilePanel;
    public JLabel timerLabel;
    public JPanel timerBufferPanel;

    public BannerPanel() {
        /// Setup Banner Panel
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(50, 50));
        setOpaque(false);

        /// Add Profile
        profilePanel = new ProfilePanel();
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
        timerLabel.setFont(AssetManager.getFont("chess_font", 32));
        timerLabel.setForeground(AssetManager.getThemeColor("text"));
        timerPanel.add(timerLabel, BorderLayout.CENTER);
    }
}

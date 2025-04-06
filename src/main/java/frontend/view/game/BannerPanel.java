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
        setBackground(AssetManager.getInstance().getThemeManager().getCurrentTheme().getColor("panel"));
        setPreferredSize(new Dimension(100, 50));
        setMinimumSize(new Dimension(100, 50));

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
        // avatarPanel.setImage(ImageLoader.loadBufferedImage("avatars/bot.png"));
        avatarBufferPanel.add(avatarPanel);

        // Add
        profilePanel.add(avatarBufferPanel, BorderLayout.WEST);

        // Add
        add(profilePanel, BorderLayout.WEST);

        /// Add Timer
        JPanel timerPanel = new JPanel(new BorderLayout());
        add(timerPanel, BorderLayout.EAST);
    }
}

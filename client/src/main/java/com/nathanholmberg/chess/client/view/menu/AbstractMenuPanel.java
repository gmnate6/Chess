package com.nathanholmberg.chess.client.view.menu;

import com.nathanholmberg.chess.client.model.SettingsManager;
import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.panels.TranslucentPanel;
import com.nathanholmberg.chess.client.view.utils.ProfilePanel;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractMenuPanel extends JPanel {
    private TranslucentPanel translucentPanel;
    private JLabel titleLabel;
    private JSeparator separator;

    public ProfilePanel profilePanel;
    protected ServerStatusPanel serverStatusPanel;

    protected JPanel contentPanel;
    protected String title;
    protected float ratio;
    protected float heightRatio = 0.7f;

    public AbstractMenuPanel(String title, float ratio) {
        setOpaque(false);
        setLayout(null);

        this.title = title;
        this.ratio = ratio;

        // Initialize Components
        initializeComponents();

        // Add a ComponentListener to adjust TransparentPanel when TitlePanel resizes
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                onResize();
            }
        });
        onResize();
    }

    protected void initializeComponents() {
        // Profile Panel
        profilePanel = new ProfilePanel();
        profilePanel.setAvatar(SettingsManager.getAvatar());
        profilePanel.setUsername(SettingsManager.getUsername());
        add(profilePanel);

        // Server Status
        serverStatusPanel = new ServerStatusPanel();
        add(serverStatusPanel);

        // Transparent Panel
        translucentPanel = new TranslucentPanel();
        translucentPanel.setLayout(new BorderLayout());
        add(translucentPanel);

        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        titlePanel.setOpaque(false);
        translucentPanel.add(titlePanel, BorderLayout.NORTH);

        // Title Label
        titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(AssetManager.getThemeColor("text"));
        titlePanel.add(Box.createVerticalGlue());
        titlePanel.add(titleLabel);

        // Spacer Line
        separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setOpaque(true);
        separator.setBackground(AssetManager.getThemeColor("text"));
        titlePanel.add(Box.createVerticalStrut(10)); // Add some space before the separator
        titlePanel.add(separator);
        titlePanel.add(Box.createVerticalStrut(10)); // Add some space after the separator

        // Content Panel
        contentPanel = new JPanel();
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        translucentPanel.add(contentPanel, BorderLayout.CENTER);
    }

    protected int getBaseFontSize() {
        return Math.max(16, getHeight() / 20);
    }

    protected void onResize() {
        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        // Set ProfilePanel Bounds
        Dimension preferredSize = profilePanel.getPreferredSize();
        profilePanel.setBounds(10, 10, preferredSize.width, preferredSize.height);

        // Set ServerStatusPanel Bounds
        preferredSize = serverStatusPanel.getPreferredSize();
        int x = getWidth() - preferredSize.width - 10;
        int y = 10;
        serverStatusPanel.setBounds(x, y, preferredSize.width, preferredSize.height);

        // Calculate the size of TransparentPanel
        int height = (int) (getHeight() * heightRatio);
        int width = (int) (height * ratio);
        height = Math.max(height, 50);
        width = Math.max(width, 50);

        // Calculate the position to center the TransparentPanel
        x = (getWidth() - width) / 2;
        y = (getHeight() - height) / 2;

        // Update TransparentPanel's bounds
        translucentPanel.setBounds(x, y, width, height);
        revalidate();
        repaint();

        // Calculate font sizes dynamically
        int baseFontSize = getBaseFontSize();
        int titleFontSize = baseFontSize * 2;

        // Update title label font size
        titleLabel.setFont(AssetManager.getFont("chess_font", titleFontSize));

        // Update separator size (80% of the panel width)
        int separatorWidth = Math.min(getWidth(), (int) (translucentPanel.getWidth() * 0.8f));
        separator.setMaximumSize(new Dimension(separatorWidth, 4));
    }
}

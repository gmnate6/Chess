package frontend.view.menu;

import frontend.model.assets.AssetManager;
import frontend.view.button.NeutralButton;
import frontend.view.button.TransparentButton;

import javax.swing.*;
import java.awt.*;

public class TitlePanel extends ContentPanel {

    private JLabel titleLabel;
    private JSeparator separator;
    private TransparentButton playOnlineButton;
    private TransparentButton playOfflineButton;
    private TransparentButton settingsButton;

    public TitlePanel() {
        super(2 / 3f);

        // Set transparentPanel's layout
        transparentPanel.setLayout(new BoxLayout(transparentPanel, BoxLayout.Y_AXIS));

        // Initialize components
        initializeComponents();

        // Add resize listener to dynamically adjust components
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                updateComponentsSize();
            }
        });

        // Initial sizing
        updateComponentsSize();
    }

    /**
     * Initializes the components and adds them to the transparentPanel.
     */
    private void initializeComponents() {
        // Title Label
        titleLabel = new JLabel("Chess", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(AssetManager.getInstance().getThemeColor("text")); // AssetManager-based text color
        transparentPanel.add(Box.createVerticalGlue());
        transparentPanel.add(titleLabel);

        // Spacer Line (Separator)
        separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setOpaque(true);
        separator.setBackground(AssetManager.getInstance().getThemeColor("text")); // AssetManager-based line color
        transparentPanel.add(Box.createVerticalStrut(10)); // Add some space before the separator
        transparentPanel.add(separator);
        transparentPanel.add(Box.createVerticalStrut(10)); // Add some space after the separator

        // Play Online Button
        playOnlineButton = new TransparentButton("Play Online");
        playOnlineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        transparentPanel.add(playOnlineButton);

        // Spacer between buttons
        transparentPanel.add(Box.createVerticalStrut(10));

        // Play Offline Button
        playOfflineButton = new TransparentButton("Play Offline");
        playOfflineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        transparentPanel.add(playOfflineButton);

        // Spacer between buttons
        transparentPanel.add(Box.createVerticalStrut(10));

        // Settings Button
        settingsButton = new TransparentButton("Settings");
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        transparentPanel.add(settingsButton);

        transparentPanel.add(Box.createVerticalGlue()); // Add vertical glue to balance spacing
    }

    /**
     * Updates the size of all components dynamically based on the size of the TitlePanel.
     */
    private void updateComponentsSize() {
        // Calculate font sizes dynamically
        int baseFontSize = Math.max(16, getHeight() / 20); // Adaptive font size based on TitlePanel height
        int titleFontSize = baseFontSize * 2; // Title font is 3x larger than base

        // Update title label font size
        titleLabel.setFont(AssetManager.getInstance().getFont("chess_font", titleFontSize));

        // Update separator size (80% of the panel width)
        int separatorWidth = Math.min(getWidth(), (int) (transparentPanel.getWidth() * 0.8f));
        separator.setMaximumSize(new Dimension(separatorWidth, 4)); // Height fixed as 4px

        // Update button font sizes
        playOnlineButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        playOfflineButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        settingsButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));

        // Revalidate and repaint the panel to apply changes
        transparentPanel.revalidate();
        transparentPanel.repaint();
    }
}
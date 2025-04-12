package frontend.view.menu;

import frontend.model.assets.AssetManager;
import frontend.view.components.button.TransparentButton;

import javax.swing.*;
import java.awt.*;

public class TitlePanel extends AbstractMenuPanel {
    public TransparentButton onlineButton;
    public TransparentButton offlineButton;
    public TransparentButton settingsButton;

    public TitlePanel() {
        super("Chess", 2 / 3f);
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Online Button
        onlineButton = new TransparentButton("Play Online");
        onlineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        onlineButton.setEnabled(false);
        contentPanel.add(onlineButton);

        // Spacer between buttons
        contentPanel.add(Box.createVerticalStrut(10));

        // Offline Button
        offlineButton = new TransparentButton("Play Offline");
        offlineButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(offlineButton);

        // Spacer between buttons
        contentPanel.add(Box.createVerticalStrut(10));

        // Settings Button
        settingsButton = new TransparentButton("Settings");
        settingsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(settingsButton);

        // Add vertical glue to balance spacing
        contentPanel.add(Box.createVerticalGlue());
    }

    @Override
    protected void onResize() {
        super.onResize();

        // Calculate font sizes dynamically
        int baseFontSize = getBaseFontSize();

        // Update button font sizes
        onlineButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        offlineButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        settingsButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));

        // Revalidate and repaint the panel to apply changes
        revalidate();
        repaint();
    }
}
package frontend.view.menu;

import frontend.model.assets.AssetManager;
import frontend.view.components.button.TransparentButton;

import javax.swing.*;
import java.awt.*;

public class TitlePanel extends AbstractMenuPanel {
    public TransparentButton onlineButton;
    public TransparentButton botButton;
    public TransparentButton soloButton;
    public TransparentButton settingsButton;

    public TitlePanel() {
        super("Chess", 2 / 3f);
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        contentPanel.setLayout(new GridLayout(0, 1, 20, 20));

        // Online Button
        onlineButton = new TransparentButton("Play Online");
        onlineButton.setEnabled(false);
        contentPanel.add(onlineButton);

        // Bot Button
        botButton = new TransparentButton("Play Bot");
        contentPanel.add(botButton);

        // Solo Button
        soloButton = new TransparentButton("Play Solo");
        contentPanel.add(soloButton);

        // Settings Button
        settingsButton = new TransparentButton("Settings");
        contentPanel.add(settingsButton);
    }

    @Override
    protected void onResize() {
        super.onResize();

        // Calculate font sizes dynamically
        int baseFontSize = getBaseFontSize();

        // Update button font sizes
        onlineButton.setFont(AssetManager.getFont("chess_font", baseFontSize));
        botButton.setFont(AssetManager.getFont("chess_font", baseFontSize));
        soloButton.setFont(AssetManager.getFont("chess_font", baseFontSize));
        settingsButton.setFont(AssetManager.getFont("chess_font", baseFontSize));

        // Revalidate and repaint the panel to apply changes
        revalidate();
        repaint();
    }
}
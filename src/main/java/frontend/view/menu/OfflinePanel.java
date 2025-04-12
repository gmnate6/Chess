package frontend.view.menu;

import frontend.model.assets.AssetManager;
import frontend.view.components.button.TransparentButton;

import javax.swing.*;
import java.awt.*;

public class OfflinePanel extends AbstractMenuPanel {
    public TransparentButton botButton;
    public TransparentButton soloButton;
    public TransparentButton backButton;

    public OfflinePanel() {
        super("Offline", 2 / 3f);
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        // Bot Button
        botButton = new TransparentButton("Play Bot");
        botButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(botButton);

        // Spacer between buttons
        contentPanel.add(Box.createVerticalStrut(10));

        // Solo Button
        soloButton = new TransparentButton("Play Solo");
        soloButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(soloButton);

        // Spacer between buttons
        contentPanel.add(Box.createVerticalStrut(10));

        // Back Button
        backButton = new TransparentButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(backButton);

        // Add vertical glue to balance spacing
        contentPanel.add(Box.createVerticalGlue());
    }

    @Override
    protected void onResize() {
        super.onResize();

        // Calculate font sizes dynamically
        int baseFontSize = getBaseFontSize();

        // Update font sizes
        botButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        soloButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        backButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));

        // Revalidate and repaint the panel to apply changes
        revalidate();
        repaint();
    }
}

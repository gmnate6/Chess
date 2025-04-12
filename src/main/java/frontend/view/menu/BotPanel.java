package frontend.view.menu;

import engine.ai.ChessAI;
import frontend.model.assets.AssetManager;
import frontend.view.components.TranslucentComboBox;
import frontend.view.components.TranslucentLabel;
import frontend.view.components.button.ColorButton;
import frontend.view.components.button.TranslucentButton;
import utils.Color;

import javax.swing.*;
import java.awt.*;

public class BotPanel extends AbstractMenuPanel {
    private JLabel botLabel;
    public JComboBox<ChessAI> botDropdown;
    private JLabel colorLabel;
    public JComboBox<Color> colorDropdown;
    public ColorButton playButton;
    public TranslucentButton backButton;

    public BotPanel() {
        super("Offline - Bot", 2f);
        heightRatio = .5f;
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        contentPanel.setLayout(new GridLayout(3, 2, 10, 10));

        // Bot Label
        botLabel = new TranslucentLabel("Bot:");
        botLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(botLabel);

        // Bot Dropdown
        botDropdown = new TranslucentComboBox<>();
        botDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(botDropdown);

        // Color Label
        colorLabel = new TranslucentLabel("Play as:");
        colorLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(colorLabel);

        // Color Dropdown
        colorDropdown = new TranslucentComboBox<>();
        colorDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(colorDropdown);

        // Play Button
        playButton = new ColorButton("Play");
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(playButton);

        // Back Button
        backButton = new TranslucentButton("Back");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(backButton);
    }

    @Override
    protected void onResize() {
        super.onResize();

        // Calculate font sizes dynamically
        int baseFontSize = (int) (getBaseFontSize() * 1.5);

        // Update font sizes
        botLabel.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        botDropdown.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        colorLabel.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        colorDropdown.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        playButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        backButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));

        // Revalidate and repaint the panel to apply changes
        revalidate();
        repaint();
    }
}

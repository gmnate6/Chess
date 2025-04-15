package com.nathanholmberg.chess.client.view.menu;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.OpaqueComboBox;
import com.nathanholmberg.chess.client.view.components.TranslucentLabel;
import com.nathanholmberg.chess.client.view.components.button.ColorButton;
import com.nathanholmberg.chess.client.view.components.button.TranslucentButton;
import com.nathanholmberg.chess.engine.ai.ChessAI;
import com.nathanholmberg.chess.engine.enums.Color;

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
        super("Play Bot", 2.1f);
        heightRatio = .47f;
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        contentPanel.setLayout(new GridLayout(3, 2, 20, 20));

        // Bot Label
        botLabel = new TranslucentLabel("Bot:");
        botLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(botLabel);

        // Bot Dropdown
        botDropdown = new OpaqueComboBox<>();
        botDropdown.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(botDropdown);

        // Color Label
        colorLabel = new TranslucentLabel("Play as:");
        colorLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(colorLabel);

        // Color Dropdown
        colorDropdown = new OpaqueComboBox<>();
        colorDropdown.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(colorDropdown);

        // Play Button
        playButton = new ColorButton("Play");
        playButton.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(playButton);

        // Back Button
        backButton = new TranslucentButton("Back");
        backButton.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(backButton);
    }

    @Override
    protected void onResize() {
        super.onResize();

        // Calculate font sizes dynamically
        int baseFontSize = getBaseFontSize();

        // Update font sizes
        botLabel.setFont(AssetManager.getFont("chess_font", baseFontSize));
        botDropdown.setFont(AssetManager.getFont("chess_font", baseFontSize));
        colorLabel.setFont(AssetManager.getFont("chess_font", baseFontSize));
        colorDropdown.setFont(AssetManager.getFont("chess_font", baseFontSize));
        playButton.setFont(AssetManager.getFont("chess_font", baseFontSize));
        backButton.setFont(AssetManager.getFont("chess_font", baseFontSize));

        // Revalidate and repaint the panel to apply changes
        revalidate();
        repaint();
    }
}

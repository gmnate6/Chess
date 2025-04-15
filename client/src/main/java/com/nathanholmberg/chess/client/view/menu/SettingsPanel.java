package com.nathanholmberg.chess.client.view.menu;

import com.nathanholmberg.chess.client.model.SettingsManager;
import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.OpaqueComboBox;
import com.nathanholmberg.chess.client.view.components.TranslucentLabel;
import com.nathanholmberg.chess.client.view.components.TranslucentTextField;
import com.nathanholmberg.chess.client.view.components.button.ColorButton;
import com.nathanholmberg.chess.client.view.components.button.TranslucentButton;
import com.nathanholmberg.chess.client.view.components.panels.DynamicImagedPanel;
import com.nathanholmberg.chess.client.view.utils.SquareLayoutManager;

import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends AbstractMenuPanel {
    private JLabel usernameLabel;
    public JTextField usernameField;

    public DynamicImagedPanel avatarPanel;
    public JComboBox<String> avatarDropdown;

    private JLabel themeLabel;
    public JComboBox<String> themeDropdown;

    private JLabel serverURLLabel;
    public JTextField serverURLField;

    public ColorButton saveButton;
    public TranslucentButton cancelButton;

    public SettingsPanel() {
        super("Settings", 3/2f);
    }

    @Override
    protected void initializeComponents() {
        super.initializeComponents();
        contentPanel.setLayout(new GridLayout(5, 2, 20, 20));

        // Username Label
        usernameLabel = new TranslucentLabel("Username:");
        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(usernameLabel);

        // Username Field
        usernameField = new TranslucentTextField();
        usernameField.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(usernameField);

        // Avatar Preview Buffer Panel
        JPanel avatarPreviewBufferPanel = new JPanel(new SquareLayoutManager());
        avatarPreviewBufferPanel.setOpaque(false);
        contentPanel.add(avatarPreviewBufferPanel);

        // Avatar Preview
        Image avatar = AssetManager.getAvatar(SettingsManager.getAvatar());
        avatarPanel = new DynamicImagedPanel(avatar);
        avatarPreviewBufferPanel.add(avatarPanel);

        // Avatar Dropdown
        avatarDropdown = new OpaqueComboBox<>();
        avatarDropdown.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(avatarDropdown);

        // Theme Label
        themeLabel = new TranslucentLabel("Theme:");
        themeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(themeLabel);

        // Theme Dropdown
        themeDropdown = new OpaqueComboBox<>();
        themeDropdown.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(themeDropdown);

        // ServerURL Label
        serverURLLabel = new TranslucentLabel("Server URL:");
        serverURLLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(serverURLLabel);

        // ServerURL Field
        serverURLField = new TranslucentTextField();
        serverURLField.setAlignmentX(CENTER_ALIGNMENT);
        serverURLField.setEditable(false); // TODO: Remove this
        contentPanel.add(serverURLField);

        // Save Button
        saveButton = new ColorButton("Save");
        saveButton.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(saveButton);

        // Cancel Button
        cancelButton = new TranslucentButton("Cancel");
        cancelButton.setAlignmentX(CENTER_ALIGNMENT);
        contentPanel.add(cancelButton);
    }

    @Override
    protected void onResize() {
        super.onResize();

        // Calculate font sizes dynamically
        int baseFontSize = (int) (getBaseFontSize() / 1.5);

        // Update font sizes
        usernameLabel.setFont(AssetManager.getFont("chess_font", baseFontSize));
        usernameField.setFont(AssetManager.getFont("chess_font", baseFontSize));
        avatarDropdown.setFont(AssetManager.getFont("chess_font", baseFontSize));
        themeLabel.setFont(AssetManager.getFont("chess_font", baseFontSize));
        themeDropdown.setFont(AssetManager.getFont("chess_font", baseFontSize));
        serverURLLabel.setFont(AssetManager.getFont("chess_font", baseFontSize));
        serverURLField.setFont(AssetManager.getFont("chess_font", baseFontSize));
        saveButton.setFont(AssetManager.getFont("chess_font", baseFontSize));
        cancelButton.setFont(AssetManager.getFont("chess_font", baseFontSize));

        // Revalidate and repaint the panel to apply changes
        revalidate();
        repaint();
    }
}

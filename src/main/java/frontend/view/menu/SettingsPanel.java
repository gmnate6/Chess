package frontend.view.menu;

import frontend.model.SettingsManager;
import frontend.model.assets.AssetManager;
import frontend.view.components.TranslucentComboBox;
import frontend.view.components.TranslucentLabel;
import frontend.view.components.TranslucentTextField;
import frontend.view.components.button.ColorButton;
import frontend.view.components.button.TranslucentButton;
import frontend.view.components.panels.DynamicImagedPanel;
import frontend.view.utils.SquareLayoutManager;

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
        contentPanel.setLayout(new GridLayout(5, 2, 10, 10));

        // Username Label
        usernameLabel = new TranslucentLabel("Username:");
        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(usernameLabel);

        // Username Field
        usernameField = new TranslucentTextField();
        usernameField.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(usernameField);

        // Avatar Preview Buffer Panel
        JPanel avatarPreviewBufferPanel = new JPanel(new SquareLayoutManager());
        avatarPreviewBufferPanel.setOpaque(false);
        contentPanel.add(avatarPreviewBufferPanel);

        // Avatar Preview
        Image avatar = AssetManager.getInstance().getAvatar(SettingsManager.getInstance().getAvatar());
        avatarPanel = new DynamicImagedPanel(avatar);
        avatarPreviewBufferPanel.add(avatarPanel);

        // Avatar Dropdown
        avatarDropdown = new TranslucentComboBox<>();
        avatarDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(avatarDropdown);

        // Theme Label
        themeLabel = new TranslucentLabel("Theme:");
        themeLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(themeLabel);

        // Theme Dropdown
        themeDropdown = new TranslucentComboBox<>();
        themeDropdown.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(themeDropdown);

        // ServerURL Label
        serverURLLabel = new TranslucentLabel("Server URL:");
        serverURLLabel.setHorizontalAlignment(SwingConstants.LEFT);
        contentPanel.add(serverURLLabel);

        // ServerURL Field
        serverURLField = new TranslucentTextField();
        serverURLField.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(serverURLField);

        // Save Button
        saveButton = new ColorButton("Save");
        saveButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(saveButton);

        // Cancel Button
        cancelButton = new TranslucentButton("Cancel");
        cancelButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(cancelButton);
    }

    @Override
    protected void onResize() {
        super.onResize();

        // Calculate font sizes dynamically
        int baseFontSize = (int) (getBaseFontSize() / 1.5);

        // Update font sizes
        usernameLabel.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        usernameField.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        avatarDropdown.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        themeLabel.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        themeDropdown.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        serverURLLabel.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        serverURLField.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        saveButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));
        cancelButton.setFont(AssetManager.getInstance().getFont("chess_font", baseFontSize));

        // Revalidate and repaint the panel to apply changes
        revalidate();
        repaint();
    }
}

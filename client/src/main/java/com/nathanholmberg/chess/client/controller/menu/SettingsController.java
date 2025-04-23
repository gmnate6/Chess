package com.nathanholmberg.chess.client.controller.menu;

import com.nathanholmberg.chess.client.controller.BaseController;
import com.nathanholmberg.chess.client.controller.MainController;
import com.nathanholmberg.chess.client.model.SettingsManager;
import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.button.CustomButton;
import com.nathanholmberg.chess.client.view.menu.SettingsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SettingsController implements BaseController {
    private final SettingsPanel settingsPanel;

    public SettingsController() {
        settingsPanel = new SettingsPanel();
        setInitialValues();
        setListeners();
    }

    private void setInitialValues() {
        // Username
        settingsPanel.usernameField.setText(SettingsManager.getUsername());

        // Avatar
        settingsPanel.avatarDropdown.removeAllItems();
        List<String> avatars = new ArrayList<>(AssetManager.getAvatars().keySet());
        Collections.sort(avatars);
        for (String avatar : avatars) {
            settingsPanel.avatarDropdown.addItem(avatar);
        }
        settingsPanel.avatarDropdown.setSelectedItem(SettingsManager.getAvatar());

        // Theme
        settingsPanel.themeDropdown.removeAllItems();
        List<String> Themes = new ArrayList<>(AssetManager.getThemeManager().getPrettyThemes());
        Collections.sort(Themes);
        for (String key : Themes) {
            settingsPanel.themeDropdown.addItem(key);
        }
        settingsPanel.themeDropdown.setSelectedItem(AssetManager.getThemeManager().getPrettyName(SettingsManager.getTheme()));

        // Server URL
        settingsPanel.serverURLField.setText(SettingsManager.getServerURL());
    }

    public void setListeners() {
        settingsPanel.avatarDropdown.addActionListener(e -> {
            String selectedAvatar = (String) settingsPanel.avatarDropdown.getSelectedItem();
            settingsPanel.avatarPanel.setImage(AssetManager.getAvatars().get(selectedAvatar));
        });

        // Save Button Listener
        settingsPanel.saveButton.addActionListener(e -> save());

        // Cancel Button Listener
        settingsPanel.cancelButton.addActionListener(e -> goBack());
    }

    private void save() {
        // Username
        String username = settingsPanel.usernameField.getText();
        if (!SettingsManager.validUsername(username)) {
            JOptionPane.showMessageDialog(settingsPanel, "Invalid username", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Avatar
        String avatar = (String) settingsPanel.avatarDropdown.getSelectedItem();
        if (!SettingsManager.validAvatar(avatar)) {
            System.err.println("Invalid avatar");
            JOptionPane.showMessageDialog(settingsPanel, "Invalid avatar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Theme
        String theme = (String) settingsPanel.themeDropdown.getSelectedItem();
        theme = AssetManager.getThemeManager().getThemeKey(theme);
        if (!SettingsManager.validTheme(theme)) {
            JOptionPane.showMessageDialog(settingsPanel, "Invalid theme", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Server URL
        String serverURL = settingsPanel.serverURLField.getText();
        if (!SettingsManager.validServerURL(serverURL)) {
            JOptionPane.showMessageDialog(settingsPanel, "Invalid server URL", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If not changes
        if (
                SettingsManager.getUsername().equals(username) &&
                SettingsManager.getAvatar().equals(avatar) &&
                SettingsManager.getTheme().equals(theme) &&
                SettingsManager.getServerURL().equals(serverURL)
        ) {
            goBack();
            return;
        }

        // Set
        SettingsManager.setUsername(username);
        SettingsManager.setAvatar(avatar);
        SettingsManager.setTheme(theme);
        SettingsManager.setServerURL(serverURL);

        // Save
        SettingsManager.save();

        // Force Redraw
        MainController.forceRedraw();

        // Exit
        goBack();
    }

    private void goBack() {
        MainController.switchTo(new TitleController());
    }

    @Override
    public void dispose() {
        // Loop through all components in the panel
        for (java.awt.Component component : settingsPanel.getComponents()) {
            // Check if the component is a button
            if (component instanceof CustomButton button) {
                button.dispose();
            }
        }
    }

    @Override
    public JPanel getPanel() {
        return settingsPanel;
    }
}

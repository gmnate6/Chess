package frontend.controller.menu;

import frontend.controller.BaseController;
import frontend.controller.MainController;
import frontend.model.SettingsManager;
import frontend.model.assets.AssetManager;
import frontend.view.components.button.CustomButton;
import frontend.view.menu.SettingsPanel;

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
        SettingsManager settingsManager = SettingsManager.getInstance();
        AssetManager assetManager = AssetManager.getInstance();

        // Username
        settingsPanel.usernameField.setText(settingsManager.getUsername());

        // Avatar
        settingsPanel.avatarDropdown.removeAllItems();
        List<String> avatars = new ArrayList<>(assetManager.getAvatars().keySet());
        Collections.sort(avatars);
        for (String avatar : avatars) {
            settingsPanel.avatarDropdown.addItem(avatar);
        }
        settingsPanel.avatarDropdown.setSelectedItem(settingsManager.getAvatar());

        // Theme
        settingsPanel.themeDropdown.removeAllItems();
        List<String> Themes = new ArrayList<>(assetManager.getThemeManager().getPrettyThemes());
        Collections.sort(Themes);
        for (String key : Themes) {
            settingsPanel.themeDropdown.addItem(key);
        }
        settingsPanel.themeDropdown.setSelectedItem(assetManager.getThemeManager().getPrettyName(settingsManager.getTheme()));

        // Server URL
        settingsPanel.serverURLField.setText(settingsManager.getServerURL());
    }

    public void setListeners() {
        settingsPanel.avatarDropdown.addActionListener(e -> {
            String selectedAvatar = (String) settingsPanel.avatarDropdown.getSelectedItem();
            settingsPanel.avatarPanel.setImage(AssetManager.getInstance().getAvatars().get(selectedAvatar));
        });

        // Save Button Listener
        settingsPanel.saveButton.addActionListener(e -> save());

        // Cancel Button Listener
        settingsPanel.cancelButton.addActionListener(e -> goBack());
    }

    private void save() {
        SettingsManager settingsManager = SettingsManager.getInstance();

        // Username
        String username = settingsPanel.usernameField.getText();
        if (!settingsManager.validUsername(username)) {
            JOptionPane.showMessageDialog(settingsPanel, "Invalid username", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Avatar
        String avatar = (String) settingsPanel.avatarDropdown.getSelectedItem();
        if (!settingsManager.validAvatar(avatar)) {
            System.err.println("Invalid avatar");
            JOptionPane.showMessageDialog(settingsPanel, "Invalid avatar", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Theme
        String theme = (String) settingsPanel.themeDropdown.getSelectedItem();
        theme = AssetManager.getInstance().getThemeManager().getThemeKey(theme);
        if (!settingsManager.validTheme(theme)) {
            JOptionPane.showMessageDialog(settingsPanel, "Invalid theme", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Server URL
        String serverURL = settingsPanel.serverURLField.getText();
        if (!settingsManager.validServerURL(serverURL)) {
            JOptionPane.showMessageDialog(settingsPanel, "Invalid server URL", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Save Settings
        String finalTheme = theme;
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                // Set
                settingsManager.setUsername(username);
                settingsManager.setAvatar(avatar);
                settingsManager.setTheme(finalTheme);
                settingsManager.setServerURL(serverURL);

                // Save
                settingsManager.save();
                return null;
            }
        }.execute();

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

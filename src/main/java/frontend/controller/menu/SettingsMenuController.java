package frontend.controller.menu;

import frontend.controller.BaseController;
import frontend.view.components.button.CustomButton;
import frontend.view.menu.SettingsPanel;

import javax.swing.*;

public class SettingsMenuController implements BaseController {
    private final SettingsPanel settingsPanel;

    public SettingsMenuController() {
        settingsPanel = new SettingsPanel();
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

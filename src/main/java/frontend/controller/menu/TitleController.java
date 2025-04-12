package frontend.controller.menu;

import frontend.controller.BaseController;
import frontend.controller.MainController;
import frontend.model.SettingsManager;
import frontend.view.components.button.CustomButton;
import frontend.view.menu.TitlePanel;

import javax.swing.*;

public class TitleController implements BaseController {
    private final TitlePanel titlePanel;

    public TitleController() {
        titlePanel = new TitlePanel();

        // Offline Button Listener
        titlePanel.offlineButton.addActionListener(
                e -> MainController.switchTo(new OfflineController())
        );

        // Settings Button Listener
        titlePanel.settingsButton.addActionListener(
                e -> MainController.switchTo(new SettingsController())
        );
    }

    @Override
    public void dispose() {
        // Loop through all components in the panel
        for (java.awt.Component component : titlePanel.getComponents()) {
            // Check if the component is a button
            if (component instanceof CustomButton button) {
                button.dispose();
            }
        }
    }

    @Override
    public JPanel getPanel() {
        return titlePanel;
    }
}

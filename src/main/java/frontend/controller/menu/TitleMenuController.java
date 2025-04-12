package frontend.controller.menu;

import frontend.controller.BaseController;
import frontend.controller.MainController;
import frontend.view.components.button.CustomButton;
import frontend.view.menu.TitlePanel;

import javax.swing.*;

public class TitleMenuController implements BaseController {
    private final TitlePanel titlePanel;

    public TitleMenuController() {
        titlePanel = new TitlePanel();

        // Settings Button Listener
        titlePanel.settingsButton.addActionListener(
                e -> MainController.getInstance().switchTo(new SettingsMenuController())
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

package frontend.controller.menu;

import frontend.controller.BaseController;
import frontend.controller.MainController;
import frontend.controller.game.SoloGameController;
import frontend.view.components.button.CustomButton;
import frontend.view.menu.OfflinePanel;

import javax.swing.*;

public class OfflineController implements BaseController {
    private final OfflinePanel offlinePanel;

    public OfflineController() {
        offlinePanel = new OfflinePanel();

        // Bot Button Listener
        offlinePanel.botButton.addActionListener(
                e -> MainController.switchTo(new BotController())
        );

        // Solo Button Listener
        offlinePanel.soloButton.addActionListener(
                e -> MainController.switchTo(new SoloGameController())
        );

        // Back Button Listener
        offlinePanel.backButton.addActionListener(
                e -> MainController.switchTo(new TitleController())
        );
    }

    @Override
    public void dispose() {
        // Loop through all components in the panel
        for (java.awt.Component component : offlinePanel.getComponents()) {
            // Check if the component is a button
            if (component instanceof CustomButton button) {
                button.dispose();
            }
        }
    }

    @Override
    public JPanel getPanel() {
        return offlinePanel;
    }
}

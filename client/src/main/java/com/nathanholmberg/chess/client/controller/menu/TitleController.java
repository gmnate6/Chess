package com.nathanholmberg.chess.client.controller.menu;

import com.nathanholmberg.chess.client.controller.BaseController;
import com.nathanholmberg.chess.client.controller.MainController;
import com.nathanholmberg.chess.client.controller.game.SoloGameController;
import com.nathanholmberg.chess.client.view.components.button.CustomButton;
import com.nathanholmberg.chess.client.view.menu.TitlePanel;

import javax.swing.*;

public class TitleController implements BaseController {
    private final TitlePanel titlePanel;

    public TitleController() {
        titlePanel = new TitlePanel();

        // Bot Button Listener
        titlePanel.botButton.addActionListener(
                e -> MainController.switchTo(new BotController())
        );

        // Solo Button Listener
        titlePanel.soloButton.addActionListener(
                e -> MainController.switchTo(new SoloGameController())
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

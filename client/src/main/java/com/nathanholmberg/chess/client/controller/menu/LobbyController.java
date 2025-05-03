package com.nathanholmberg.chess.client.controller.menu;

import com.nathanholmberg.chess.client.controller.BaseController;
import com.nathanholmberg.chess.client.controller.MainController;
import com.nathanholmberg.chess.client.view.components.button.CustomButton;
import com.nathanholmberg.chess.client.view.menu.LobbyPanel;

import javax.swing.*;

public class LobbyController implements BaseController {
    private final LobbyPanel lobbyPanel;

    public LobbyController() {
        lobbyPanel = new LobbyPanel();

        // Back Button Listener
        lobbyPanel.backButton.addActionListener(
                e -> MainController.switchTo(new TitleController())
        );
    }

    @Override
    public void dispose() {
        // Loop through all components in the panel
        for (java.awt.Component component : lobbyPanel.getComponents()) {
            // Check if the component is a button
            if (component instanceof CustomButton button) {
                button.dispose();
            }
        }
    }

    @Override
    public JPanel getPanel() {
        return lobbyPanel;
    }
}
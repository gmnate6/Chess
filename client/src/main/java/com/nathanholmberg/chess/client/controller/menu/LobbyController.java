package com.nathanholmberg.chess.client.controller.menu;

import com.nathanholmberg.chess.client.controller.BaseController;
import com.nathanholmberg.chess.client.controller.MainController;
import com.nathanholmberg.chess.client.controller.game.OnlineGameController;
import com.nathanholmberg.chess.client.model.websocket.LobbyWebSocketManager;
import com.nathanholmberg.chess.client.view.components.button.CustomButton;
import com.nathanholmberg.chess.client.view.menu.LobbyPanel;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.ChessTimer;

import javax.swing.*;

public class LobbyController implements BaseController {
    private final LobbyPanel lobbyPanel;
    private final LobbyWebSocketManager lobbyWebSocketManager;

    public LobbyController() {
        lobbyPanel = new LobbyPanel();

        // Back Button Listener
        lobbyPanel.backButton.addActionListener(
                e -> MainController.switchTo(new TitleController())
        );

        // Connect to Lobby
        lobbyWebSocketManager = new LobbyWebSocketManager();
        lobbyWebSocketManager.connect();

        // Handle Game Ready Message
        lobbyWebSocketManager.setGameReadyListener((String gameId, Color color) -> {
            MainController.switchTo(new OnlineGameController(color, gameId));
        });
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
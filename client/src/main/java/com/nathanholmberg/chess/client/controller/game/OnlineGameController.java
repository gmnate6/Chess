package com.nathanholmberg.chess.client.controller.game;

import com.nathanholmberg.chess.client.model.websocket.GameWebSocketManager;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.types.Move;

public class OnlineGameController extends AbstractGameController {
    GameWebSocketManager gameWebSocketManager;

    public OnlineGameController(String gameId, Color color) {
        super(color, null);
        configureOnlineGameSettings();

        gameWebSocketManager = new GameWebSocketManager(gameId, color);
    }

    private void configureOnlineGameSettings() {
        gamePanel.drawButton.setEnabled(false); // TODO: FOR NOW
        gamePanel.rematchButton.setEnabled(false);
    }

    @Override
    public void executeMove(Move move) {
        super.executeMove(move);
    }
}

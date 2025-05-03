package com.nathanholmberg.chess.client.controller.game;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.types.Move;

public class OnlineGameController extends AbstractGameController {
    public OnlineGameController(Color color, String gameId, String opponentUsername) {
        super(color, null);
        configureOnlineGameSettings();
    }

    private void configureOnlineGameSettings() {
        gamePanel.drawButton.setEnabled(false);
        gamePanel.rematchButton.setEnabled(false);
    }

    @Override
    public void executeMove(Move move) {
        super.executeMove(move);
    }
}

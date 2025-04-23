package com.nathanholmberg.chess.client.controller.game.managers;

import com.nathanholmberg.chess.client.controller.game.AbstractGameController;
import com.nathanholmberg.chess.client.view.components.TranslucentLabel;
import com.nathanholmberg.chess.client.view.game.HistoryPanel;

import java.util.ArrayList;

public class HistoryManager {
    private final AbstractGameController gameController;
    private final HistoryPanel historyPanel;

    private final ArrayList<TranslucentLabel> moveLabels = new ArrayList<>();

    public HistoryManager(AbstractGameController gameController, HistoryPanel historyPanel) {
        this.gameController = gameController;
        this.historyPanel = historyPanel;
    }

    public void selectMove(int moveIndex) {
        if (moveIndex < 0 || moveIndex >= moveLabels.size()) {
            historyPanel.selectMove(null);
            return;
        }

        historyPanel.selectMove(moveLabels.get(moveIndex));
    }

    public void addMove(String move) {
        TranslucentLabel moveLabel = historyPanel.addMove(move);
        moveLabels.add(moveLabel);

        int moveIndex = moveLabels.size() - 1;
        selectMove(moveIndex);
        moveLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectMove(moveIndex);
                if (!gameController.inPlay()) {
                    gameController.loadGameStateAt(moveIndex);
                }
            }
        });
    }
}

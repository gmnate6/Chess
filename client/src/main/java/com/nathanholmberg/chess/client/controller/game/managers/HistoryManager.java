package com.nathanholmberg.chess.client.controller.game.managers;

import com.nathanholmberg.chess.client.controller.game.AbstractGameController;
import com.nathanholmberg.chess.client.view.components.TranslucentLabel;
import com.nathanholmberg.chess.client.view.game.HistoryPanel;

import java.util.ArrayList;

public class HistoryManager {
    private final AbstractGameController gameController;
    private final HistoryPanel historyPanel;
    private int currentMoveIndex = -1;

    private final ArrayList<TranslucentLabel> moveLabels = new ArrayList<>();

    public HistoryManager(AbstractGameController gameController, HistoryPanel historyPanel) {
        this.gameController = gameController;
        this.historyPanel = historyPanel;
    }

    public void selectMove(int moveIndex) {
        currentMoveIndex = moveIndex;

        if (moveIndex < 0) {
            historyPanel.jumpToTop();
        } else if (moveIndex >= moveLabels.size() - 1) {
            historyPanel.jumpToBottom();
        }

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
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (moveIndex == currentMoveIndex) {
                    return;
                }
                selectMove(moveIndex);
                gameController.loadGameStateAt(moveIndex);
            }
        });
    }
}

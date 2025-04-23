package com.nathanholmberg.chess.client.controller.game.listeners;

import com.nathanholmberg.chess.client.controller.game.AbstractGameController;
import com.nathanholmberg.chess.client.view.game.BoardPanel;
import com.nathanholmberg.chess.engine.types.Position;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardMouseListener extends MouseAdapter {
    private final AbstractGameController controller;

    public BoardMouseListener(AbstractGameController controller) {
        this.controller = controller;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Make sure game is current
        if (!controller.isAtLastMove()) {
            controller.stepFullForward();
            return;
        }

        BoardPanel boardPanel = controller.gamePanel.boardPanel;
        Position position = boardPanel.pointToPosition(e.getPoint());

        if (SwingUtilities.isLeftMouseButton(e)) {
            controller.onSquareButtonLeftDown(position);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            controller.onSquareButtonRightDown(position);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // Make sure game is current
        if (!controller.isAtLastMove()) {
            controller.stepFullForward();
            return;
        }

        BoardPanel boardPanel = controller.gamePanel.boardPanel;
        Position position = boardPanel.pointToPosition(e.getPoint());

        if (SwingUtilities.isLeftMouseButton(e)) {
            controller.onSquareButtonLeftUp(position);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            controller.onSquareButtonRightUp(position);
        }
    }
}

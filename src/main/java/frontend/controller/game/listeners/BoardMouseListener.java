package frontend.controller.game.listeners;

import engine.types.Position;
import frontend.controller.game.AbstractGameController;
import frontend.view.game.BoardPanel;

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
        BoardPanel boardPanel = controller.gamePanel.boardPanel;
        Position position = boardPanel.pointToPosition(e.getPoint());

        if (SwingUtilities.isLeftMouseButton(e)) {
            controller.onSquareButtonLeftUp(position);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            controller.onSquareButtonRightUp(position);
        }
    }
}

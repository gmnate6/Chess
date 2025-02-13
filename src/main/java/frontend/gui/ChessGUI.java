package frontend.gui;

import frontend.gui.panels.BoardPanel;
import frontend.Color;

import javax.swing.*;
import java.awt.*;

public class ChessGUI extends JFrame {
    public BoardPanel boardPanel;

    public ChessGUI() {
        // Setup
        setLayout(new GridBagLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(800, 800));
        setLocationRelativeTo(null);
        setBackground(new java.awt.Color(0xE5E5E5));

        // Add Board
        boardPanel = new BoardPanel(Color.WHITE);
        boardPanel.getSquareButton(0, 0).setPiece('P');
        add(boardPanel);

        // Show Frame
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessGUI gui = new ChessGUI();
        });
    }
}

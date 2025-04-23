package com.nathanholmberg.chess.client.view.game;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.TranslucentLabel;
import com.nathanholmberg.chess.client.view.components.TranslucentScrollPane;
import com.nathanholmberg.chess.client.view.components.panels.TranslucentPanel;

import javax.swing.*;
import java.awt.*;

public class HistoryPanel extends TranslucentPanel {
    int rowHeight = 32;
    private JPanel currentRow;
    private JPanel movesPanel;
    private TranslucentLabel selectedMoveLabel;

    private final TranslucentScrollPane scrollPane;

    public HistoryPanel() {
        super();
        setLayout(new BorderLayout());

        scrollPane = new TranslucentScrollPane(rowHeight);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void selectMove(TranslucentLabel label) {
        if (selectedMoveLabel != null) {
            selectedMoveLabel.setTranslucent(false);
        }

        selectedMoveLabel = label;

        if (label != null) {
            selectedMoveLabel.setTranslucent(true);
        }

        revalidate();
        repaint();
    }

    public void jumpToBottom() {
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setValue(bar.getMaximum());
    }

    public void jumpToTop() {
        JScrollBar bar = scrollPane.getVerticalScrollBar();
        bar.setValue(bar.getMinimum());
    }

    public TranslucentLabel addMove(String move) {
        boolean completed = false;

        if (currentRow == null) {
            currentRow = new JPanel(new BorderLayout());
            currentRow.setOpaque(false);
            currentRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));

            // Number Label
            JLabel countLabel = new JLabel(scrollPane.getRowCount() + ".");
            countLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
            countLabel.setHorizontalAlignment(SwingConstants.LEFT);
            countLabel.setVerticalAlignment(SwingConstants.CENTER);
            countLabel.setFont(AssetManager.getFont("chess_font", 16));
            countLabel.setForeground(AssetManager.getThemeColor("text"));
            countLabel.setOpaque(false);
            countLabel.setPreferredSize(new Dimension(40, rowHeight));
            currentRow.add(countLabel, BorderLayout.WEST);

            // Moves Panel
            movesPanel = new JPanel(new GridLayout(1,2));
            movesPanel.setOpaque(false);
            currentRow.add(movesPanel, BorderLayout.CENTER);

            scrollPane.addRow(currentRow);
        } else {
            completed = true;
        }

        // Move Label Buffer Panel
        JPanel moveLabelBufferPanel = new JPanel(new BorderLayout());
        moveLabelBufferPanel.setOpaque(false);
        moveLabelBufferPanel.setBorder(BorderFactory.createEmptyBorder(2, 10, 2, 10));
        movesPanel.add(moveLabelBufferPanel);

        // Move Label
        TranslucentLabel moveLabel = new TranslucentLabel(move);
        moveLabel.setVerticalAlignment(SwingConstants.CENTER);
        moveLabel.setFontSize(16);
        moveLabel.setMinimumSize(new Dimension(50, rowHeight));
        moveLabel.setHorizontalAlignment(SwingConstants.CENTER);
        moveLabelBufferPanel.add(moveLabel, BorderLayout.WEST);

        if (completed) {
            currentRow = null;
            movesPanel = null;
        }

        // Update Scroll Pane
        scrollPane.revalidate();
        scrollPane.repaint();
        SwingUtilities.invokeLater(this::jumpToBottom);

        return moveLabel;
    }
}

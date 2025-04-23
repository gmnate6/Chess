package com.nathanholmberg.chess.client.view.components;

import com.nathanholmberg.chess.client.view.components.panels.TranslucentPanel;
import com.nathanholmberg.chess.client.view.components.scrollbar.TranslucentScrollBar;

import javax.swing.*;
import java.awt.*;

public class TranslucentScrollPane extends JScrollPane {
    private final int rowHeight;
    private int rowCount = 1;
    private final JPanel contentPanel;

    public TranslucentScrollPane(int rowHeight) {
        this.rowHeight = rowHeight;

        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
        contentPanel.setOpaque(false);

        setViewportView(contentPanel);
        setOpaque(false);
        setBorder(null);
        setViewportBorder(null);
        getViewport().setOpaque(false);
        getViewport().setBackground(new Color(0, 0, 0, 0));

        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        setVerticalScrollBar(new TranslucentScrollBar(Adjustable.VERTICAL, 32));
    }

    public void addRow(JPanel panel) {
        rowCount++;

        // Alternate Background
        JPanel rowContainer;
        if (rowCount % 2 == 0) {
            rowContainer = new JPanel(new BorderLayout());
            rowContainer.setOpaque(false);
        } else {
            rowContainer = new TranslucentPanel(new BorderLayout());
        }

        rowContainer.setAlignmentX(Component.LEFT_ALIGNMENT);
        rowContainer.setPreferredSize(new Dimension(0, rowHeight));
        rowContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));

        rowContainer.add(panel, BorderLayout.CENTER);
        contentPanel.add(rowContainer);
    }

    public int getRowCount() {
        return rowCount;
    }
}

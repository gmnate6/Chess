package com.nathanholmberg.chess.client.view.components;

import com.nathanholmberg.chess.client.view.components.panels.TranslucentPanel;
import com.nathanholmberg.chess.client.view.components.scrollbar.TranslucentScrollBar;

import javax.swing.*;
import java.awt.*;

public class TranslucentScrollPane extends JScrollPane {
    private final int rowHeight;
    private int rowCount = 0;
    private final JPanel contentPanel;

    public TranslucentScrollPane(int rowHeight) {
        this.rowHeight = rowHeight;

        contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
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

        rowContainer.add(panel, BorderLayout.CENTER);
        rowContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, rowHeight));
        contentPanel.add(rowContainer);
    }

    public int getRowCount() {
        return rowCount;
    }
}

package com.nathanholmberg.chess.client.view.components.panels;

import com.nathanholmberg.chess.client.view.utils.TranslucentBackgroundPainter;

import javax.swing.*;
import java.awt.*;

public class TranslucentPanel extends JPanel {
    public TranslucentPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }
    public TranslucentPanel() {
        this(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        TranslucentBackgroundPainter.paintNormal(g, getWidth(), getHeight());
    }
}

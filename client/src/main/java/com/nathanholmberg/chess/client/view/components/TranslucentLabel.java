package com.nathanholmberg.chess.client.view.components;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.utils.TranslucentBackgroundPainter;

import javax.swing.*;
import java.awt.*;

public class TranslucentLabel extends JLabel {
    public TranslucentLabel() {
        super();
        setOpaque(false);
        setFontSize(32);
        setForeground(AssetManager.getThemeColor("text"));
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.CENTER);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    public TranslucentLabel(String text) {
        this();
        setText(text);
    }

    public void setFontSize(int size) {
        setFont(AssetManager.getFont("chess_font", size));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        TranslucentBackgroundPainter.paintNormal(g, getWidth(), getHeight());
    }
}

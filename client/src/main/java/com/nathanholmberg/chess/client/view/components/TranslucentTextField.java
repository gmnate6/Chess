package com.nathanholmberg.chess.client.view.components;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.utils.TranslucentBackgroundPainter;

import javax.swing.*;
import java.awt.*;

public class TranslucentTextField extends JTextField {
    public TranslucentTextField() {
        super();
        setOpaque(false);
        setForeground(AssetManager.getThemeColor("text"));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        TranslucentBackgroundPainter.paintNormal(g, getWidth(), getHeight());
    }
}

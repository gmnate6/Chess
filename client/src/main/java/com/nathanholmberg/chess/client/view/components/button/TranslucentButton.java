package com.nathanholmberg.chess.client.view.components.button;

import com.nathanholmberg.chess.client.view.utils.TranslucentBackgroundPainter;

import java.awt.*;

public class TranslucentButton extends CustomButton {
    public TranslucentButton() {
        super();
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
    }
    public TranslucentButton(String text) {
        this();
        setText(text);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getModel().isPressed()) {
            TranslucentBackgroundPainter.paintDarker(g, getWidth(), getHeight());
            return;
        }
        TranslucentBackgroundPainter.paintNormal(g, getWidth(), getHeight());
    }
}

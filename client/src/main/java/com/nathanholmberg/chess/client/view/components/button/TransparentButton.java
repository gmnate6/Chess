package com.nathanholmberg.chess.client.view.components.button;

public class TransparentButton extends CustomButton {
    public TransparentButton() {
        super();
        setOpaque(false);
        setBackground(new java.awt.Color(0, 0, 0, 0));
    }
    public TransparentButton(String text) {
        this();
        setText(text);
    }
}

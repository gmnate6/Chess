package frontend.view.components.button;

import frontend.model.assets.AssetManager;

import javax.swing.*;

public class ColorButton extends CustomButton {
    public ColorButton() {
        super();
        setForeground(AssetManager.getInstance().getThemeColor("text"));
        setBackground(AssetManager.getInstance().getThemeColor("color"));
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(true);
        setFocusPainted(false);
        repaint();
    }
    public ColorButton(String text) {
        this();
        setText(text);
    }
}

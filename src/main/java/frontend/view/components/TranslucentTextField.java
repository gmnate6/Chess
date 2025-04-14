package frontend.view.components;

import frontend.model.assets.AssetManager;
import frontend.view.utils.TranslucentBackgroundPainter;

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

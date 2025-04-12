package frontend.view.components;

import frontend.model.assets.AssetManager;
import frontend.view.utils.TranslucentBackgroundPainter;

import javax.swing.*;
import java.awt.*;

public class TranslucentComboBox<E> extends JComboBox<E> {
    public TranslucentComboBox() {
        super();
        configure();
    }
    public TranslucentComboBox(E[] items) {
        super(items);
        configure();
    }

    private void configure() {
        setOpaque(false);
        setForeground(AssetManager.getInstance().getThemeColor("text"));
        setBackground(Color.BLACK);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        TranslucentBackgroundPainter.paintNormal(g, getWidth(), getHeight());
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Optionally, you can customize the border
        g.setColor(Color.LIGHT_GRAY);
        g.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
    }
}

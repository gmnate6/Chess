package frontend.view.button;

import frontend.model.assets.AssetManager;

import javax.swing.*;
import java.awt.*;

public class Button extends JButton {
    public Button(String text, Color defaultColor, Color clickedColor) {
        super(text);

        setFont(AssetManager.getInstance().getFont("chess_font", 16));
        setForeground(AssetManager.getInstance().getThemeColor("text"));
        setBackground(defaultColor);
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(true);
        setFocusPainted(false);
        repaint();

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                setBackground(defaultColor.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                setBackground(defaultColor);
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!isEnabled()) { return; }
                setBackground(clickedColor);
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                setBackground(defaultColor.darker());
            }

        });
    }

    public void setGlyph(int glyph) {
        setFont(AssetManager.getInstance().getFont("chess_glyph", 16));
        new String(Character.toChars(glyph));
    }
}

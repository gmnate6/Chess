package frontend.view.button;

import frontend.model.assets.AssetManager;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    public int fontSize = 32;
    public final Color defaultColor;
    public final Color clickedColor;

    public CustomButton(String text, Color defaultColor, Color clickedColor) {
        setText(text);
        setForeground(AssetManager.getInstance().getThemeColor("text"));
        setBackground(defaultColor);
        setBorder(BorderFactory.createEmptyBorder());
        setOpaque(true);
        setFocusPainted(false);
        repaint();

        this.defaultColor = defaultColor;
        this.clickedColor = clickedColor;

        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!isEnabled()) { return; }
                setBackground(defaultColor.darker());
                repaint();
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!isEnabled()) { return; }
                setBackground(defaultColor);
                repaint();
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent evt) {
                if (!isEnabled()) { return; }
                setBackground(clickedColor);
                repaint();
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                if (!isEnabled()) { return; }
                setBackground(defaultColor.darker());
                repaint();
            }
        });
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setBackground(defaultColor);
        } else {
            setBackground(defaultColor.darker());
        }
        repaint();
    }

    public void setGlyph(int glyph) {
        setFont(AssetManager.getInstance().getFont("chess_glyph", fontSize));
        super.setText(new String(Character.toChars(glyph)));
    }

    public void setText(String text) {
        setFont(AssetManager.getInstance().getFont("chess_font", fontSize));
        super.setText(text);
    }
}

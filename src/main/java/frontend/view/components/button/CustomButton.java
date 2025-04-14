package frontend.view.components.button;

import frontend.model.assets.AssetManager;

import javax.swing.*;
import java.awt.*;

public class CustomButton extends JButton {
    public int fontSize = 32;
    public final Color textColor = AssetManager.getThemeColor("text");

    public CustomButton() {
        super();
        setForeground(textColor);
        setFont(AssetManager.getFont("chess_font", fontSize));
        setBorderPainted(false);
        setFocusPainted(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        repaint();
    }
    public CustomButton(String text) {
        this();
        setText(text);
    }

    public void dispose() {
        // Remove all ActionListeners
        for (var listener : this.getActionListeners()) {
            this.removeActionListener(listener);
        }

        // Remove all MouseListeners
        for (var listener : this.getMouseListeners()) {
            this.removeMouseListener(listener);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setForeground(textColor);
        } else {
            setForeground(textColor.darker());
        }
        repaint();
    }
}

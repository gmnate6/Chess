package frontend.view.components.scrollbar;

import frontend.model.assets.AssetManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class TranslucentScrollBar extends JScrollBar {
    public TranslucentScrollBar(int orientation, int rowHeight) {
        super(orientation);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setUnitIncrement(rowHeight);

        setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = AssetManager.getInstance().getThemeColor("translucent");
                trackColor = new Color(0, 0, 0, 0);
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }

            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
    }

    public TranslucentScrollBar(int orientation) {
        this(orientation, 16);
    }

    public TranslucentScrollBar() {
        this(VERTICAL, 16);
    }
}
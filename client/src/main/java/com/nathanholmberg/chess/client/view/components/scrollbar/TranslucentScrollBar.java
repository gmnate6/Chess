package com.nathanholmberg.chess.client.view.components.scrollbar;

import com.nathanholmberg.chess.client.model.assets.AssetManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class TranslucentScrollBar extends JScrollBar {
    public TranslucentScrollBar(int orientation, int unitIncrement) {
        super(orientation);
        setOpaque(false);
        setBackground(new Color(0, 0, 0, 0));
        setUnitIncrement(unitIncrement);

        setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = AssetManager.getThemeColor("translucent");
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
}
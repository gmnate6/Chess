package com.nathanholmberg.chess.client.view.components.scrollbar;

import com.nathanholmberg.chess.client.model.assets.AssetManager;

import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;

public class OpaqueScrollBar extends JScrollBar {
    public OpaqueScrollBar(int orientation, int rowHeight) {
        super(orientation);
        setUnitIncrement(rowHeight);

        setUI(new BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                thumbColor = AssetManager.getThemeColor("translucent");
                trackColor = AssetManager.getThemeColor("opaque");
            }

            @Override
            protected JButton createDecreaseButton(int orientation) {
                return makeZeroButton();
            }

            @Override
            protected JButton createIncreaseButton(int orientation) {
                return makeZeroButton();
            }

            private JButton makeZeroButton() {
                JButton btn = new JButton();
                btn.setPreferredSize(new Dimension(0, 0));
                btn.setMinimumSize(new Dimension(0, 0));
                btn.setMaximumSize(new Dimension(0, 0));
                return btn;
            }
        });
    }
}
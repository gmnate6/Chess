package frontend.view.components;

import frontend.model.assets.AssetManager;
import frontend.view.components.scrollbar.OpaqueScrollBar;
import frontend.view.utils.TranslucentBackgroundPainter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class OpaqueComboBox<E> extends JComboBox<E> {
    public OpaqueComboBox() {
        super();
        configure();
    }
    public OpaqueComboBox(E[] items) {
        super(items);
        configure();
    }

    private void configure() {
        setOpaque(false);
        setForeground(AssetManager.getInstance().getThemeColor("text"));
        setBackground(AssetManager.getInstance().getThemeColor("opaque"));

        setUI(new BasicComboBoxUI() {
            // Arrow
            @Override
            protected JButton createArrowButton() {
                JButton arrow = new JButton("?");
                arrow.setFont(AssetManager.getInstance().getFont("chess_glyph", 16));
                arrow.setForeground(AssetManager.getInstance().getThemeColor("text"));
                arrow.setBackground(AssetManager.getInstance().getThemeColor("opaque"));
                arrow.setFocusable(false);
                arrow.setHorizontalAlignment(SwingConstants.CENTER);

                // Border
                arrow.setBorder(BorderFactory.createLineBorder(
                        AssetManager.getInstance().getThemeColor("text"), 1));

                return arrow;
            }

            // Scroll Bar
            @Override
            protected ComboPopup createPopup() {
                return new BasicComboPopup(comboBox) {
                    @Override
                    protected JScrollPane createScroller() {
                        JScrollPane sp = super.createScroller();
                        OpaqueScrollBar bar = new OpaqueScrollBar(Adjustable.VERTICAL, 16);

                        sp.setVerticalScrollBar(bar);
                        sp.setOpaque(false);
                        sp.getViewport().setOpaque(false);
                        sp.getViewport().setBackground(AssetManager.getInstance().getThemeColor("opaque"));

                        return sp;
                    }
                };
            }
        });
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

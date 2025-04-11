package frontend.view.menu;

import frontend.model.assets.AssetManager;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

// **Translucent Glass Panel with Rounded Borders**
public class TransparentPanel extends JPanel {
    public TransparentPanel(LayoutManager layout) {
        super(layout);
        setOpaque(false);
    }
    public TransparentPanel() {
        this(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing for smooth curves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set a translucent white color
        g2.setColor(AssetManager.getInstance().getThemeColor("transparent"));

        // Draw rounded rectangle
        int arcSize = 25;
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcSize, arcSize));

        g2.dispose();
    }
}

package frontend.view.menu;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

// **Translucent Glass Panel with Rounded Borders**
class TranslucentPanel extends JPanel {
    private final int alpha = 240;

    public TranslucentPanel(int width, int height) {
        setPreferredSize(new Dimension(width, height));
        setOpaque(false);
    }
    public TranslucentPanel(double width, double height) {
        this((int) width, (int) height);
    }
    public TranslucentPanel(float width, float height) {
        this((int) width, (int) height);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing for smooth curves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set a translucent white color
        g2.setColor(new Color(255, 255, 255, alpha)); // 150 = Semi-transparent

        // Draw rounded rectangle
        int arcSize = 100; // Curve radius
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), arcSize, arcSize));

        g2.dispose();
    }
}

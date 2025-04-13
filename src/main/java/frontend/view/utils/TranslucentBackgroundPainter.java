package frontend.view.utils;

import frontend.model.assets.AssetManager;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class TranslucentBackgroundPainter {
    private static final Color color = AssetManager.getInstance().getThemeColor("translucent");
    private static final int arcSize = 10;

    private static void paint(Graphics g, int width, int height, Color color) {
        Graphics2D g2 = (Graphics2D) g.create();

        // Enable antialiasing for smooth curves
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set Color
        g2.setColor(color);

        // Draw rounded rectangle
        g2.fill(new RoundRectangle2D.Float(0, 0, width, height, arcSize, arcSize));

        // Dispose
        g2.dispose();
    }

    public static void paintBrighter(Graphics g, int width, int height) {
        paint(g, width, height, color.brighter());
    }

    public static void paintNormal(Graphics g, int width, int height) {
        paint(g, width, height, color);
    }

    public static void paintDarker(Graphics g, int width, int height) {
        paint(g, width, height, color.darker());
    }
}

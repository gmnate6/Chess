package com.nathanholmberg.chess.client.view.components.panels;

import javax.swing.*;
import java.awt.*;

public abstract class AbstractImagedPanel extends JPanel {
    protected Image originalImage;
    protected Image cachedImage = null;
    protected int cachedWidth = -1;
    protected int cachedHeight = -1;

    public AbstractImagedPanel() {
        super();
        setLayout(null);
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Cannot draw null
        if (originalImage == null) { return; }

        // Get Point
        int x = getNewPoint().x;
        int y = getNewPoint().y;

        // Get Dimension
        int newWidth = getNewDimension().width;
        int newHeight = getNewDimension().height;
        if (newWidth == 0 || newHeight == 0) { return; }

        // Update cachedImage if size changed
        if (hasChangedSize()) {
            cachedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            cachedWidth = newWidth;
            cachedHeight = newHeight;
        }
        g.drawImage(cachedImage, x, y, newWidth, newHeight, this);
    }

    protected abstract Point getNewPoint();

    protected abstract Dimension getNewDimension();

    protected boolean hasChangedSize() {
        int width = getNewDimension().width;
        int height = getNewDimension().height;
        return (cachedImage == null || width != cachedWidth || height != cachedHeight);
    }

    public void setImage(Image image) {
        this.originalImage = image;
        forceRedraw();
    }

    public void forceRedraw() {
        cachedImage = null;
        cachedWidth = -1;
        cachedHeight = -1;
        this.repaint();
    }
}

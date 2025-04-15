package com.nathanholmberg.chess.client.view.components.panels;

import java.awt.*;

public class BackgroundImagedPanel extends AbstractImagedPanel {
    public BackgroundImagedPanel() {
        super();
    }

    protected Point getNewPoint() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Get New Dimension
        Dimension newDimension = getNewDimension();

        // Return Point
        int xOffset = (panelWidth - newDimension.width) / 2;
        int yOffset = (panelHeight - newDimension.height) / 2;
        return new Point(xOffset, yOffset);
    }

    protected Dimension getNewDimension() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();

        // Get the dimensions of the original image
        int imageWidth = originalImage.getWidth(null);
        int imageHeight = originalImage.getHeight(null);

        // Calculate the scale factors for both dimensions
        double scaleX = (double) panelWidth / imageWidth;
        double scaleY = (double) panelHeight / imageHeight;

        // Use the larger scale factor to ensure the image covers the panel
        double scale = Math.max(scaleX, scaleY);

        // Return Dimension
        int newImageWidth = (int) (imageWidth * scale);
        int newImageHeight = (int) (imageHeight * scale);
        return new Dimension(newImageWidth, newImageHeight);
    }
}

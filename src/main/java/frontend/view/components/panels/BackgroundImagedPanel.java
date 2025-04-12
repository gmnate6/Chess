package frontend.view.components.panels;

import javax.swing.*;
import java.awt.*;

public class BackgroundImagedPanel extends JPanel {
    private Image originalImage;
    private Image cachedImage = null;
    private int cachedWidth = -1;
    private int cachedHeight = -1;

    public BackgroundImagedPanel(Image image) {
        super();
        setImage(image);
    }
    public BackgroundImagedPanel() {
        super();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        if (panelWidth == 0 || panelHeight == 0 || originalImage == null) {
            return;
        }

        // Get the dimensions of the original image
        int imageWidth = originalImage.getWidth(null);
        int imageHeight = originalImage.getHeight(null);

        // Calculate the scale factors for both dimensions
        double scaleX = (double) panelWidth / imageWidth;
        double scaleY = (double) panelHeight / imageHeight;

        // Use the larger scale factor to ensure the image covers the panel
        double scale = Math.max(scaleX, scaleY);

        int newImageWidth = (int) (imageWidth * scale);
        int newImageHeight = (int) (imageHeight * scale);

        // Calculate offsets to center the image
        int xOffset = (panelWidth - newImageWidth) / 2;
        int yOffset = (panelHeight - newImageHeight) / 2;

        // Update cachedImage if necessary
        if (hasChangedSize(newImageWidth, newImageHeight)) {
            cachedImage = originalImage.getScaledInstance(newImageWidth, newImageHeight, Image.SCALE_SMOOTH);
            cachedWidth = newImageWidth;
            cachedHeight = newImageHeight;
        }

        // Draw the image centered within the panel
        g.drawImage(cachedImage, xOffset, yOffset, this);
    }


    protected boolean hasChangedSize(int newWidth, int newHeight) {
        return (cachedImage == null || cachedWidth != newWidth || cachedHeight != newHeight);
    }

    public void setImage(Image image) {
        this.originalImage = image;
        cachedImage = null;
        cachedWidth = -1;
        cachedHeight = -1;
        this.repaint();
    }
}

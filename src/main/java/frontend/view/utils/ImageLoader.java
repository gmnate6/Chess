package frontend.view.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static final String basePath = "images/";

    public static BufferedImage loadBufferedImage(String path) {
        String fullPath = basePath + path;
        try (InputStream imageStream = ImageLoader.class.getClassLoader().getResourceAsStream(fullPath))
        {
            return ImageIO.read(imageStream);
        } catch (Exception e) {
            System.err.println("Error loading image: " + fullPath);
        }

        // Return Orange BufferedImage
        int size = 100;
        BufferedImage blankImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = blankImage.createGraphics();
        g2d.setColor(Color.ORANGE);
        g2d.fillRect(0, 0, size, size);
        g2d.dispose();
        return blankImage;
    }

    public static ImageIcon getImageIcon(String path) {
        path = basePath + path;
        try {
            InputStream imageStream = ImageLoader.class.getClassLoader().getResourceAsStream(path);

            // Throw Early
            if (imageStream == null) {
                throw new RuntimeException("Image not found: " + path);
            }

            // Return an ImageIcon instead of Image
            return new ImageIcon(ImageIO.read(imageStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static BufferedImage iconToBufferedImage(ImageIcon icon) {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.drawImage(icon.getImage(), 0, 0, null);
        g2d.dispose();
        return image;
    }

    // Overlay two ImageIcons and return the resulting ImageIcon
    public static ImageIcon overlayIcons(ImageIcon baseIcon, ImageIcon overlayIcon) {
        // Convert the icons to BufferedImages
        BufferedImage baseImage = iconToBufferedImage(baseIcon);
        BufferedImage overlayImage = iconToBufferedImage(overlayIcon);

        // Get dimensions
        int baseWidth = baseImage.getWidth();
        int baseHeight = baseImage.getHeight();
        int overlayWidth = overlayImage.getWidth();
        int overlayHeight = overlayImage.getHeight();

        // Create a new BufferedImage to hold the combined result
        BufferedImage combined = new BufferedImage(baseWidth, baseHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = combined.createGraphics();

        // Draw the base image
        g2d.drawImage(baseImage, 0, 0, null);

        // Calculate position to center overlay
        int x = (baseWidth - overlayWidth) / 2;
        int y = (baseHeight - overlayHeight) / 2;

        // Draw overlay centered on base image
        g2d.drawImage(overlayImage, x, y, null);
        g2d.dispose();

        // Convert the combined BufferedImage back to an ImageIcon
        return new ImageIcon(combined);
    }
}
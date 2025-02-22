package frontend.view.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static final String basePath = "images/";

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

    public static ImageIcon getPieceIcon(char pieceChar) {
        String file = null;
        switch (pieceChar) {
            // White
            case 'P':
                file = "white_pawn.png";
                break;
            case 'R':
                file = "white_rook.png";
                break;
            case 'N':
                file = "white_knight.png";
                break;
            case 'B':
                file = "white_bishop.png";
                break;
            case 'Q':
                file = "white_queen.png";
                break;
            case 'K':
                file = "white_king.png";
                break;
            // Black
            case 'p':
                file = "black_pawn.png";
                break;
            case 'r':
                file = "black_rook.png";
                break;
            case 'n':
                file = "black_knight.png";
                break;
            case 'b':
                file = "black_bishop.png";
                break;
            case 'q':
                file = "black_queen.png";
                break;
            case 'k':
                file = "black_king.png";
                break;
        }

        // If pieceChar is not valid
        if (file == null) {
            throw new RuntimeException("Illegal PieceChar: '" + pieceChar + "' is not valid.");
        }

        return getImageIcon("pieces/" + file);
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

    // Rotates an image by a given angle (in degrees)
    public ImageIcon getRotatedImageIcon(ImageIcon icon, double angle) {
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        // Create a buffered image with transparency
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();

        // Perform rotation using AffineTransform
        AffineTransform transform = new AffineTransform();
        transform.rotate(Math.toRadians(angle), width / 2.0, height / 2.0);
        transform.translate((bufferedImage.getWidth() - width) / 2.0, (bufferedImage.getHeight() - height) / 2.0);
        g2d.setTransform(transform);

        // Draw the original image
        g2d.drawImage(icon.getImage(), 0, 0, null);
        g2d.dispose();

        // Return a new rotated ImageIcon
        return new ImageIcon(bufferedImage);
    }
}
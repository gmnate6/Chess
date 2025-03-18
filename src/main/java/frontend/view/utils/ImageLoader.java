package frontend.view.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage loadBufferedImage(String path) {
        try (InputStream imageStream = ImageLoader.class.getClassLoader().getResourceAsStream(path))
        {
            assert imageStream != null;
            return ImageIO.read(imageStream);
        } catch (Exception e) {
            System.err.println("Error loading image: " + path);
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
}
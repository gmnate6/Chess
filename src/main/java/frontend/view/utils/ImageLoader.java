package frontend.view.utils;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage loadBufferedImage(String path) {
        if (path.endsWith(".svg")) {
            throw new IllegalArgumentException("Use loadSVGBufferedImage for SVG files");
        }

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

    public static BufferedImage renderSVGToBufferedImage(String path, int width, int height) {
        if (!path.endsWith(".svg")) {
            throw new IllegalArgumentException("Path must end with .svg");
        }

        final BufferedImage[] imagePointer = new BufferedImage[1]; // To store the rendered image

        try (InputStream svgInputStream = ImageLoader.class.getClassLoader().getResourceAsStream(path)) {
            if (svgInputStream == null) {
                throw new IllegalArgumentException("Could not find SVG file: " + path);
            }

            // Batik ImageTranscoder to convert SVG to BufferedImage
            ImageTranscoder transcoder = new ImageTranscoder() {
                @Override
                public BufferedImage createImage(int w, int h) {
                    return new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
                }

                @Override
                public void writeImage(BufferedImage img, TranscoderOutput output) {
                    imagePointer[0] = img; // Save the created image
                }
            };

            // Set transcoding hints for dimensions
            transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, (float) width);
            transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, (float) height);

            // Transcode the SVG
            TranscoderInput input = new TranscoderInput(svgInputStream);
            transcoder.transcode(input, null);

        } catch (TranscoderException e) {
            System.err.println("Error transcoding SVG file: " + path);
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error loading SVG file: " + path);
            e.printStackTrace();
        }

        // If the SVG could not be loaded/rendered, return an orange image (fallback)
        if (imagePointer[0] == null) {
            int size = Math.max(width, height); // Use the max requested dimension for the fallback
            BufferedImage blankImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = blankImage.createGraphics();
            g2d.setColor(Color.ORANGE);
            g2d.fillRect(0, 0, size, size);
            g2d.dispose();
            return blankImage;
        }

        return imagePointer[0];

    }
}
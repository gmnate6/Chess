package frontend.view.assets;

import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageManager {
    private final Map<String, BufferedImage> images = new HashMap<>();
    private final static BufferedImage fallbackImage = new BufferedImage(
            1, 1, BufferedImage.TYPE_INT_RGB) {{
        setRGB(0, 0, 0xe64100);
    }};

    private static BufferedImage loadRasterImage(String path) {
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

        // Return Fallback
        return fallbackImage;
    }

    public static BufferedImage renderSvgImage(String path, int width, int height) {
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

        // Return rendered SVG
        if (imagePointer[0] != null) {
            return imagePointer[0];
        }

        // Return Fallback
        return fallbackImage;
    }

    public void loadImage(String key, String path) {

    }

    public BufferedImage getImage(String key) { return images.get(key); }
}

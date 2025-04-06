package frontend.model.assets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.Map;

public class AvatarManager {
    private final ImageManager avatars = new ImageManager();

    public AvatarManager() {
        loadAvatars();
    }

    private void loadAvatars() {
        String path = "avatars/";
        int size = 100;

        // Load Avatars
        avatars.loadSVG("default", path + "default.svg", size, size);
        avatars.loadSVG("bot", path + "bot.svg", size, size);

        // Load the rest
        int index = 1;
        while (true) {
            String fileName = path + index + ".svg";
            InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);

            // Ran out
            if (stream == null) {
                break;
            }

            // Try to load
            avatars.loadSVG(Integer.toString(index), fileName, size, size);
            index++;
        }
    }

    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        }
        return "";
    }

    public BufferedImage getAvatar(String key) {
        if (!avatars.getImages().containsKey(key)) {
            System.err.println("Avatar with key '" + key + "' not found.");
            return null;
        }
        return avatars.getImage(key);
    }

    public Map<String, BufferedImage> getAvatars() {
        return avatars.getImages();
    }
}

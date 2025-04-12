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

    private void loadAvatar(String fileName) {
        String path = "avatars/" + fileName + ".svg";
        int size = 100;
        avatars.loadSVG(fileName, path, size, size);
    }

    private void loadAvatars() {
        loadAvatar("bot");
        loadAvatar("Boy_1");
        loadAvatar("Boy_2");
        loadAvatar("Boy_3");
        loadAvatar("Boy_4");
        loadAvatar("Boy_5");
        loadAvatar("Boy_6");
        loadAvatar("Boy_7");
        loadAvatar("Boy_8");
        loadAvatar("Boy_9");
        loadAvatar("default");
        loadAvatar("Girl_1");
        loadAvatar("Girl_2");
        loadAvatar("Girl_3");
        loadAvatar("Girl_4");
        loadAvatar("Girl_5");
        loadAvatar("Girl_6");
        loadAvatar("Girl_7");
        loadAvatar("Girl_8");
        loadAvatar("Girl_9");
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

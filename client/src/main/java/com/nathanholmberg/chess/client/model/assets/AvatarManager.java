package com.nathanholmberg.chess.client.model.assets;

import java.awt.image.BufferedImage;
import java.util.Map;

public class AvatarManager {
    private final ImageManager avatars;
    private final String path = "avatars/";

    public AvatarManager() {
        avatars = new ImageManager();
        loadAvatars();
    }

    private void loadAvatar(String fileName) {
        String fullPath = path + fileName + ".svg";
        int size = 300;
        avatars.loadSVG(fileName, fullPath, size, size);
    }

    private void loadAvatars() {
        loadAvatar("bot");
        loadAvatar("default");
        avatars.loadImage("engine", path + "engine.png");

        loadAvatar("Boy_1");
        loadAvatar("Boy_2");
        loadAvatar("Boy_3");
        loadAvatar("Boy_4");
        loadAvatar("Boy_5");
        loadAvatar("Boy_6");
        loadAvatar("Boy_7");
        loadAvatar("Boy_8");
        loadAvatar("Boy_9");
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

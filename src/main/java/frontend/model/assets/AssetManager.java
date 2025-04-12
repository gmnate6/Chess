package frontend.model.assets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Map;

public class AssetManager {
    private static AssetManager instance;
    private final AvatarManager avatarManager;
    private final CursorManager cursorManager;
    private final FontManager fontManager;
    private final SoundManager soundManager;
    private final ThemeManager themeManager;

    private AssetManager() {
        avatarManager = new AvatarManager();
        cursorManager = new CursorManager();
        fontManager = new FontManager();
        soundManager = new SoundManager();
        themeManager = new ThemeManager();
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public BufferedImage getAvatar(String key) { return avatarManager.getAvatar(key); }
    public Map<String, BufferedImage> getAvatars() { return avatarManager.getAvatars(); }

    public Cursor getCursor(String key) {
        return cursorManager.getCursor(key);
    }
    public Font getFont(String key, int size) {
        return fontManager.getFont(key, size);
    }
    public void playSound(String key) {
        soundManager.playSound(key);
    }

    public ThemeManager getThemeManager() { return themeManager; }
    public BufferedImage getThemeImage(String key) {
        return themeManager.getImage(key);
    }
    public BufferedImage getThemeImage(Character key) {
        return themeManager.getImage(key.toString());
    }
    public Color getThemeColor(String key) {
        return themeManager.getColor(key);
    }
}

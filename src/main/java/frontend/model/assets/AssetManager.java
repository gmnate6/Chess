package frontend.model.assets;

import java.awt.*;
import java.awt.image.BufferedImage;

public class AssetManager {
    private static AssetManager instance;
    private final AvatarManager avatarManager = new AvatarManager();
    private final CursorManager cursorManager = new CursorManager();
    private final FontManager fontManager = new FontManager();
    private final SoundManager soundManager = new SoundManager();
    private final ThemeManager themeManager = new ThemeManager();

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    // public AvatarManager getAvatarManager() { return avatarManager; }
    // public CursorManager getCursorManager() { return cursorManager; }
    // public FontManager getFontManager() { return fontManager; }
    // public SoundManager getSoundManager() { return soundManager; }
    // public ThemeManager getThemeManager() { return themeManager; }

    public BufferedImage getAvatar(String key) {
        return avatarManager.getAvatar(key);
    }

    public Cursor getCursor(String key) {
        return cursorManager.getCursor(key);
    }

    public Font getFont(String key, int size) {
        return fontManager.getFont(key, size);
    }

    public void playSound(String key) {
        soundManager.playSound(key);
    }

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

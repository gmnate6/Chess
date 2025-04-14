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

    private static final BufferedImage icon = ImageManager.loadRasterImage("icon.png");

    private AssetManager() {
        instance = this;
        avatarManager = new AvatarManager();
        cursorManager = new CursorManager();
        fontManager = new FontManager();
        soundManager = new SoundManager();
        themeManager = new ThemeManager();
    }

    public static void initialize() {
        if (instance != null) {
            throw new IllegalStateException("AssetManager has already been initialized.");
        }
        new AssetManager();
    }

    private static AssetManager getInstance() {
        if (instance == null) {
            System.err.println("Warning: AssetManager not initialized before use");
            initialize();
        }
        return instance;
    }

    // ─── Avatar ─────────────────────────────────────────────
    public static BufferedImage getAvatar(String key) {
        return getInstance().avatarManager.getAvatar(key);
    }

    public static Map<String, BufferedImage> getAvatars() {
        return getInstance().avatarManager.getAvatars();
    }
    // ────────────────────────────────────────────────────────

    // ─── Cursor ─────────────────────────────────────────────
    public static Cursor getCursor(String key) {
        return getInstance().cursorManager.getCursor(key);
    }
    // ────────────────────────────────────────────────────────

    // ─── Font ───────────────────────────────────────────────
    public static Font getFont(String key, int size) {
        return getInstance().fontManager.getFont(key, size);
    }
    // ────────────────────────────────────────────────────────

    // ─── Sound ──────────────────────────────────────────────
    public static void playSound(String key) {
        getInstance().soundManager.playSound(key);
    }
    // ────────────────────────────────────────────────────────

    // ─── Static Icon ────────────────────────────────────────
    public static BufferedImage getIcon() {
        return icon;
    }
    // ────────────────────────────────────────────────────────

    // ─── Theme ──────────────────────────────────────────────
    public static ThemeManager getThemeManager() {
        return getInstance().themeManager;
    }

    public static BufferedImage getThemeImage(String key) {
        return getInstance().themeManager.getImage(key);
    }

    public static BufferedImage getThemeImage(Character key) {
        return getInstance().themeManager.getImage(key.toString());
    }

    public static Color getThemeColor(String key) {
        return getInstance().themeManager.getColor(key);
    }
    // ────────────────────────────────────────────────────────
}

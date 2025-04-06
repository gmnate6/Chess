package frontend.model.assets;

public class AssetManager {
    private static AssetManager instance;
    private final AvatarManager avatarManager = new AvatarManager();
    private final CursorManager cursorManager = new CursorManager();
    private final FontManager fontManager = new FontManager();
    private final SoundManager soundManager = new SoundManager();
    private final ThemeManager themeManager = ThemeManager.getInstance();

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public AvatarManager getAvatarManager() { return avatarManager; }
    public CursorManager getCursorManager() { return cursorManager; }
    public FontManager getFontManager() { return fontManager; }
    public SoundManager getSoundManager() { return soundManager; }
    public ThemeManager getThemeManager() { return themeManager; }
}

package frontend.model.assets;

import frontend.model.json.ThemeJsonHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class ThemeManager {
    private String defaultTheme;
    private List<String> availableThemes;
    private Map<String, String> themeNames;
    private Theme currentTheme;

    public ThemeManager() {
        ThemeJsonHandler themeJsonHandler = ThemeJsonHandler.load("themes/themes.json");
        setDefaultTheme(themeJsonHandler.getDefaultTheme());
        setAvailableThemes(themeJsonHandler.getAvailableThemes());
        setThemeNames(themeJsonHandler.getThemeNames());
        loadDefaultTheme();
    }

    public String getThemeNameKey(String name) {
        Map<String, String> reversedMap = new java.util.HashMap<>();
        for (Map.Entry<String, String> entry : themeNames.entrySet()) {
            reversedMap.put(entry.getValue(), entry.getKey());
        }
        return reversedMap.get(name);
    }

    private void setDefaultTheme(String defaultTheme) { this.defaultTheme = defaultTheme; }
    private void setAvailableThemes(List<String> availableThemes) { this.availableThemes = availableThemes; }
    private void setThemeNames(Map<String, String> themeNames) { this.themeNames = themeNames; }

    public String getDefaultTheme() { return defaultTheme; }
    public List<String> getAvailableThemes() { return availableThemes; }
    public Map<String, String> getThemeNames() { return themeNames; }

    public void loadTheme(String themeName) {
        if (!availableThemes.contains(themeName)) {
            throw new IllegalArgumentException("Invalid theme name: " + themeName);
        }
        currentTheme = new Theme(themeName);
    }
    public void loadDefaultTheme() { loadTheme(defaultTheme); }
    public Theme getCurrentTheme() { return currentTheme; }

    public BufferedImage getImage(String key) { return currentTheme.getImage(key); }
    public BufferedImage getImage(Character key) { return currentTheme.getImage(key.toString()); }
    public Color getColor(String key) { return currentTheme.getColor(key); }
}

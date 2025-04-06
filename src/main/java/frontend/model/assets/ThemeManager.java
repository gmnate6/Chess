package frontend.model.assets;

import com.google.gson.Gson;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ThemeManager {
    private String default_theme;
    private List<String> available_themes;
    private Map<String, String> theme_names;
    private Theme currentTheme;

    public static ThemeManager getInstance() {
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(ThemeManager.class.getClassLoader().getResourceAsStream("themes/themes.json")))
        ) {
            Gson gson = new Gson();
            ThemeManager themeManager = gson.fromJson(reader, ThemeManager.class);
            themeManager.loadDefaultTheme();
            return themeManager;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load themes", e);
        }
    }

    public void setDefault_theme(String default_theme) { this.default_theme = default_theme; }
    public void setAvailable_themes(List<String> available_themes) { this.available_themes = available_themes; }
    public void setTheme_names(Map<String, String> theme_names) { this.theme_names = theme_names; }

    public String getDefault_theme() { return default_theme; }
    public List<String> getAvailable_themes() { return available_themes; }
    public Map<String, String> getTheme_names() { return theme_names; }

    public void loadTheme(String themeName) {
        if (!available_themes.contains(themeName)) {
            throw new IllegalArgumentException("Invalid theme name: " + themeName);
        }
        currentTheme = new Theme(themeName);
    }
    public void loadDefaultTheme() { loadTheme(default_theme); }
    public Theme getCurrentTheme() { return currentTheme; }

    public BufferedImage getImage(String key) { return currentTheme.getImage(key); }
    public BufferedImage getImage(Character key) { return currentTheme.getImage(key.toString()); }
    public Color getColor(String key) { return currentTheme.getColor(key); }
}

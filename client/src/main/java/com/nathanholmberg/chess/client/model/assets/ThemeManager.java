package com.nathanholmberg.chess.client.model.assets;

import com.nathanholmberg.chess.client.model.SettingsManager;
import com.nathanholmberg.chess.client.model.json.ThemesJsonHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ThemeManager {
    private Map<String, String> themes;
    private Theme currentTheme;

    public ThemeManager() {
        // Load from json
        ThemesJsonHandler themesJsonHandler = ThemesJsonHandler.load("themes/themes.json");
        setThemes(themesJsonHandler.getThemes());

        // Load Default
        loadTheme(SettingsManager.DEFAULT_THEME);
    }

    private void setThemes(Map<String, String> themes) {
        this.themes = themes;
    }

    public List<String> getThemes() {
        return new ArrayList<>(themes.keySet());
    }

    public List<String> getPrettyThemes() {
        return new ArrayList<>(themes.values());
    }

    public String getPrettyName(String themeName) {
        return themes.get(themeName);
    }

    public String getThemeKey(String prettyName) {
        for (Map.Entry<String, String> entry : themes.entrySet()) {
            if (entry.getValue().equals(prettyName)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public void loadTheme(String themeName) {
        if (!getThemes().contains(themeName)) {
            throw new IllegalArgumentException("Invalid theme name: " + themeName);
        }
        currentTheme = new Theme(themeName);
    }

    public BufferedImage getImage(String key) { return currentTheme.getImage(key); }
    public Color getColor(String key) { return currentTheme.getColor(key); }
}

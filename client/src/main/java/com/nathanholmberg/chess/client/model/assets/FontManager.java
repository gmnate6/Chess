package com.nathanholmberg.chess.client.model.assets;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FontManager {
    private final Map<String, Font> fonts;

    public FontManager() {
        fonts = new HashMap<>();
        loadFonts();
    }

    private void loadFont(String key, String path) {
        try {
            // Load the font from a file or resource
            Font font = Font.createFont(Font.TRUETYPE_FONT, Objects.requireNonNull(getClass().getResourceAsStream(path)));

            // Add to the map (stored with the default size)
            fonts.put(key, font);
        } catch (Exception e) {
            System.err.println("Failed to load font from file: " + path);
        }
    }

    public void loadFonts() {
        loadFont("chess_font", "/fonts/chess_font.ttf");
        loadFont("chess_glyph", "/fonts/chess_glyph.ttf");
    }

    public Font getFont(String key, float size) {
        Font font = fonts.get(key);

        if (font == null) {
            throw new IllegalArgumentException("Font with key '" + key + "' not found.");
        }

        // Return the font with the requested size
        return font.deriveFont(size);
    }
}

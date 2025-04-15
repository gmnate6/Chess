package com.nathanholmberg.chess.client.model.assets;

import com.nathanholmberg.chess.client.model.json.ColorsJsonHandler;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class ColorManager {
    private final Map<String, Color> colors;

    public ColorManager() {
        colors = new HashMap<>();
    }

    private static Color parseColor(String hex) {
        hex = hex.replace("#", "");

        // Without Alpha
        if (hex.length() == 6) {
            int r = Integer.parseInt(hex.substring(0, 2), 16);
            int g = Integer.parseInt(hex.substring(2, 4), 16);
            int b = Integer.parseInt(hex.substring(4, 6), 16);
            return new Color(r, g, b);
        }

        // With Alpha
        if (hex.length() == 8) {
            int a = Integer.parseInt(hex.substring(0, 2), 16);
            int r = Integer.parseInt(hex.substring(2, 4), 16);
            int g = Integer.parseInt(hex.substring(4, 6), 16);
            int b = Integer.parseInt(hex.substring(6, 8), 16);
            return new Color(r, g, b, a);
        }

        throw new IllegalArgumentException("Invalid color format: " + hex);
    }

    public void loadColor(String key, String hex) {
        colors.put(key, parseColor(hex));
    }

    public void loadColor(String key, Color color) {
        colors.put(key, color);
    }

    public void loadColors(String path) {
        Map<String, String> rawColorMap = ColorsJsonHandler.load(path);
        for (Map.Entry<String, String> entry : rawColorMap.entrySet()) {
            loadColor(entry.getKey(), entry.getValue());
        }
    }

    public Color getColor(String key) {
        if (!colors.containsKey(key)) {
            System.err.println("Color with key '" + key + "' not found.\n");
            Thread.dumpStack();
            return Color.BLACK;
        }
        Color color = colors.get(key);
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }
}

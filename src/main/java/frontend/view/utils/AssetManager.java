package frontend.view.utils;

import java.awt.*;
import java.util.List;
import java.awt.image.BufferedImage;
import java.util.*;

public class AssetManager {
    private static AssetManager instance;
    private final Map<String, BufferedImage> images;
    private final Map<String, Color> colors;

    private AssetManager() {
        images = new HashMap<>();
        colors = new HashMap<>();
        loadTheme("icy");
    }

    public static AssetManager getInstance() {
        if (instance == null) {
            instance = new AssetManager();
        }
        return instance;
    }

    public void loadTheme(String theme) {
        // Default
        if (theme == null || theme.equals("default")) {
            PropertiesLoader propertiesLoader = new PropertiesLoader("themes/themes.properties");
            theme = propertiesLoader.getProperty("default_theme");
        }

        // Load Theme
        clearAssets();
        loadAssets(theme);
    }

    private void clearAssets() {
        images.clear();
        colors.clear();
    }

    private boolean doesThemeExist(String theme) {
        PropertiesLoader propertiesLoader = new PropertiesLoader("themes/themes.properties");
        String themes = propertiesLoader.getProperty("available_themes");
        List<String> themeList = Arrays.asList(themes.split(","));
        return themeList.contains(theme);
    }

    private Color getColorFromProperty(PropertiesLoader propertiesLoader, String baseKey) {
        try {
            // Hex
            if (propertiesLoader.containsKey(baseKey + ".hex")) {
                String hex = propertiesLoader.getProperty(baseKey + ".hex");
                if (hex != null) {
                    return Color.decode(hex);
                }
            }

            // RGB
            if (propertiesLoader.containsKey(baseKey + ".rgb") && propertiesLoader.containsKey(baseKey + ".alpha")) {
                String rgbString = propertiesLoader.getProperty(baseKey + ".rgb");
                int alpha = Integer.parseInt(propertiesLoader.getProperty(baseKey + ".alpha"));
                if (rgbString != null) {
                    int[] rgb = Arrays.stream(rgbString.split(",")).mapToInt(Integer::parseInt).toArray();
                    return new Color(rgb[0], rgb[1], rgb[2], alpha);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        // Error
        System.err.println("Could not read color:\n\tColor: " + baseKey + "\n\tProperties: " + propertiesLoader);
        return Color.ORANGE;
    }

    private void loadAssets(String theme) {
        if (!doesThemeExist(theme)) {
            throw new RuntimeException("Theme does not exist: " + theme);
        }

        // Load Images from Theme
        images.put("P", ImageLoader.loadBufferedImage("themes/" + theme + "/images/wp.png"));
        images.put("R", ImageLoader.loadBufferedImage("themes/" + theme + "/images/wr.png"));
        images.put("N", ImageLoader.loadBufferedImage("themes/" + theme + "/images/wn.png"));
        images.put("B", ImageLoader.loadBufferedImage("themes/" + theme + "/images/wb.png"));
        images.put("Q", ImageLoader.loadBufferedImage("themes/" + theme + "/images/wq.png"));
        images.put("K", ImageLoader.loadBufferedImage("themes/" + theme + "/images/wk.png"));
        images.put("p", ImageLoader.loadBufferedImage("themes/" + theme + "/images/bp.png"));
        images.put("r", ImageLoader.loadBufferedImage("themes/" + theme + "/images/br.png"));
        images.put("n", ImageLoader.loadBufferedImage("themes/" + theme + "/images/bn.png"));
        images.put("b", ImageLoader.loadBufferedImage("themes/" + theme + "/images/bb.png"));
        images.put("q", ImageLoader.loadBufferedImage("themes/" + theme + "/images/bq.png"));
        images.put("k", ImageLoader.loadBufferedImage("themes/" + theme + "/images/bk.png"));
        images.put("board", ImageLoader.loadBufferedImage("themes/" + theme + "/images/board.png"));
        images.put("background", ImageLoader.loadBufferedImage("themes/" + theme + "/images/background.png"));
        images.put("hint", ImageLoader.loadBufferedImage("themes/" + theme + "/images/hint.png"));
        images.put("capture_hint", ImageLoader.loadBufferedImage("themes/" + theme + "/images/capture_hint.png"));

        // Load Colors from Theme
        PropertiesLoader propertiesLoader = new PropertiesLoader("themes/" + theme + "/colors.properties");
        colors.put("markedRed", getColorFromProperty(propertiesLoader, "markedRed"));
        colors.put("highlight", getColorFromProperty(propertiesLoader, "highlight"));
        colors.put("panel", getColorFromProperty(propertiesLoader, "panel"));
        colors.put("transparentButton", getColorFromProperty(propertiesLoader, "transparentButton"));
        colors.put("transparentButtonHovered", getColorFromProperty(propertiesLoader, "transparentButtonHovered"));
        colors.put("opaqueButton", getColorFromProperty(propertiesLoader, "opaqueButton"));
        colors.put("opaqueButtonHovered", getColorFromProperty(propertiesLoader, "opaqueButtonHovered"));
        colors.put("text", getColorFromProperty(propertiesLoader, "text"));
    }

    public BufferedImage getImage(String key) { return images.get(key); }
    public BufferedImage getImage(Character key) { return getImage(key.toString()); }

    public Color getColor(String key) {
        return colors.get(key);
    }
}

package frontend.model.assets;

import com.google.gson.Gson;

import java.awt.*;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ColorManager {
    private final Map<String, Color> colors = new HashMap<>();

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

    public void loadColors(String path) {
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)))
        ) {

            Gson gson = new Gson();
            Map<String, String> colorMap = gson.fromJson(reader, Map.class);

            for (Map.Entry<String, String> entry : colorMap.entrySet()) {
                loadColor(entry.getKey(), entry.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Color getColor(String key) {
        return colors.get(key);
    }
}

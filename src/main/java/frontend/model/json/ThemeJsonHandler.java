package frontend.model.json;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ThemeJsonHandler {
    private String default_theme;
    private List<String> available_themes;
    private Map<String, String> theme_names;

    public static ThemeJsonHandler load(String path) {
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(ThemeJsonHandler.class.getClassLoader().getResourceAsStream(path)))
        ) {
            Gson gson = new Gson();
            ThemeJsonHandler themeJsonHandler = gson.fromJson(reader, ThemeJsonHandler.class);
            return themeJsonHandler;
        } catch (Exception e) {
            throw new RuntimeException("Failed to load json: " + path, e);
        }
    }

    public void setDefault_theme(String default_theme) { this.default_theme = default_theme; }
    public void setAvailable_themes(List<String> available_themes) { this.available_themes = available_themes; }
    public void setTheme_names(Map<String, String> theme_names) { this.theme_names = theme_names; }

    public String getDefault_theme() { return default_theme; }
    public List<String> getAvailable_themes() { return available_themes; }
    public Map<String, String> getTheme_names() { return theme_names; }
}

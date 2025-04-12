package frontend.model.json;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ThemeJsonHandler {
    private String defaultTheme;
    private List<String> availableThemes;
    private Map<String, String> themeNames;

    public static ThemeJsonHandler load(String path) {
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(ThemeJsonHandler.class.getClassLoader().getResourceAsStream(path)))
        ) {
            Gson gson = new Gson();
            return gson.fromJson(reader, ThemeJsonHandler.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load json: " + path, e);
        }
    }

    public void setDefaultTheme(String defaultTheme) { this.defaultTheme = defaultTheme; }
    public void setAvailableThemes(List<String> availableThemes) { this.availableThemes = availableThemes; }
    public void setThemeNames(Map<String, String> themeNames) { this.themeNames = themeNames; }

    public String getDefaultTheme() { return defaultTheme; }
    public List<String> getAvailableThemes() { return availableThemes; }
    public Map<String, String> getThemeNames() { return themeNames; }
}

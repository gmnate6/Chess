package frontend.model.json;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ThemeJsonHandler {
    private Map<String, String> themes;

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

    public Map<String, String> getThemes() { return themes; }
}

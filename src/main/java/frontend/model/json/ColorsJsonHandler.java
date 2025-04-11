package frontend.model.json;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ColorsJsonHandler {
    public static Map<String, String> load(String path) {
        try (InputStreamReader reader = new InputStreamReader(
                Objects.requireNonNull(ColorsJsonHandler.class.getClassLoader().getResourceAsStream(path)))) {
            Gson gson = new Gson();
            Map<String, String> rawMap = gson.fromJson(reader, Map.class);

            // Return
            return rawMap;
        } catch (Exception e) {
            System.err.println("Failed to load colors from file: " + path);
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}

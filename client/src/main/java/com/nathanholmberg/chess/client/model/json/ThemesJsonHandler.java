package com.nathanholmberg.chess.client.model.json;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

public class ThemesJsonHandler {
    private static final Gson GSON = new Gson();
    private Map<String, String> themes = Collections.emptyMap();

    private ThemesJsonHandler() {}

    public static ThemesJsonHandler load(String path) {
        Objects.requireNonNull(path, "path must not be null");
        ClassLoader loader = ThemesJsonHandler.class.getClassLoader();

        // 1) open the resource
        try (InputStream is = loader.getResourceAsStream(path)) {
            if (is == null) {
                throw new IllegalStateException("Resource not found on classpath: " + path);
            }

            // 2) wrap in a Reader with explicit UTF‑8
            try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
                ThemesJsonHandler handler = GSON.fromJson(reader, ThemesJsonHandler.class);

                // 3) guard against a null or missing "themes" key
                if (handler == null || handler.themes == null) {
                    handler = new ThemesJsonHandler();
                }

                // 4) make the map immutable to prevent outside mutation
                handler.themes = Collections.unmodifiableMap(handler.themes);
                return handler;
            }

        } catch (JsonIOException | JsonSyntaxException e) {
            throw new IllegalStateException("Invalid JSON in resource: " + path, e);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load JSON resource: " + path, e);
        }
    }

    public Map<String, String> getThemes() { return themes; }
}

package com.nathanholmberg.chess.client.model.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

public class ColorsJsonHandler {
    private static final Gson gson = new Gson();
    private static final Type MAP_STRING_STRING = new TypeToken<Map<String, String>>() {}.getType();

    public static Map<String, String> load(String path) {
        // Try to open the resource
        InputStream is = ColorsJsonHandler.class
                .getClassLoader()
                .getResourceAsStream(path);
        if (is == null) {
            System.err.println("Resource not found on classpath: " + path);
            return Collections.emptyMap();
        }

        // Parse it with a Reader, specifying UTF‑8
        try (Reader reader = new InputStreamReader(is, StandardCharsets.UTF_8)) {
            // Now Gson knows the exact generic type
            return gson.fromJson(reader, MAP_STRING_STRING);
        } catch (JsonSyntaxException jse) {
            System.err.println("Invalid JSON syntax in " + path + ": " + jse.getMessage());
        } catch (Exception e) {
            System.err.println("Failed to load colors from " + path + ": " + e.getMessage() + "\n" + e);
        }

        return Collections.emptyMap();
    }
}

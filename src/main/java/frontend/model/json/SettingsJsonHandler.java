package frontend.model.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SettingsJsonHandler {
    private static final String PATH = "settings.json";
    private String username;
    private String avatar;
    private String theme;
    private String serverURL;

    public static SettingsJsonHandler load() {
        Path filePath = resolvePath();

        if (!Files.exists(filePath)) {
            return new SettingsJsonHandler();
        }

        try (Reader reader = Files.newBufferedReader(filePath, UTF_8)) {
            return new Gson().fromJson(reader, SettingsJsonHandler.class);
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to load JSON from file: " + filePath, e
            );
        }
    }

    public void save() {
        Path filePath = resolvePath();

        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(filePath, UTF_8)) {
                new GsonBuilder()
                        .setPrettyPrinting()
                        .create()
                        .toJson(this, writer);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Failed to save JSON to file: " + filePath, e
            );
        }
    }

    private static Path resolvePath() {
        Path p = Paths.get(SettingsJsonHandler.PATH);
        return p.isAbsolute()
                ? p
                : Paths.get(System.getProperty("user.dir")).resolve(p);
    }

    public String getUsername() { return username; }
    public String getAvatar() { return avatar; }
    public String getTheme() { return theme; }
    public String getServerURL() { return serverURL; }

    public void setUsername(String username) { this.username = username; }
    public void setAvatar(String avatar) { this.avatar = avatar; }
    public void setTheme(String theme) { this.theme = theme; }
    public void setServerURL(String serverURL) { this.serverURL = serverURL; }
}

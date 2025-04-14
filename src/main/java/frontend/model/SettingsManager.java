package frontend.model;

import frontend.model.assets.AssetManager;
import frontend.model.json.SettingsJsonHandler;

import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingsManager {
    private static SettingsManager instance;

    // ─── Defaults ───────────────────────────────────────────────
    public static final String DEFAULT_USERNAME   = "Guest";
    public static final String DEFAULT_AVATAR     = "default";
    public static final String DEFAULT_THEME      = "wood";
    public static final String DEFAULT_SERVER_URL = "ws://localhost:8080";
    // ────────────────────────────────────────────────────────────

    private String username;
    private String avatar;
    private String theme;
    private String serverURL;

    private SettingsManager() {
        instance = this;

        // 1) Load Defaults
        loadDefaults();

        // 2) Load From Disk
        load();
    }

    public static void initialize() {
        if (instance != null) {
            throw new IllegalStateException("AssetManager has already been initialized.");
        }
        new SettingsManager();
    }

    private static SettingsManager getInstance() {
        if (instance == null) {
            System.err.println("Warning: SettingsManager not initialized before use");
            initialize();
        }
        return instance;
    }

    // ─── Save & Load ────────────────────────────────────────
    private static void loadDefaults() {
        setUsername(DEFAULT_USERNAME);
        setAvatar(DEFAULT_AVATAR);
        setTheme(DEFAULT_THEME);
        setServerURL(DEFAULT_SERVER_URL);
    }

    public static void load() {
        // 1) Read From Disk
        SettingsJsonHandler fromDisk;
        try {
            fromDisk = SettingsJsonHandler.load();
        } catch (UncheckedIOException e) {
            return;
        }

        // 2) Write From Disk
        if (validUsername(fromDisk.getUsername())) {
            setUsername(fromDisk.getUsername());
        }
        if (validAvatar(fromDisk.getAvatar())) {
            setAvatar(fromDisk.getAvatar());
        }
        if (validTheme(fromDisk.getTheme())) {
            setTheme(fromDisk.getTheme());
        }
        if (validServerURL(fromDisk.getServerURL())) {
            setServerURL(fromDisk.getServerURL());
        }
    }

    public static void save() {
        SettingsJsonHandler toDisk = new SettingsJsonHandler();
        toDisk.setUsername(getUsername());
        toDisk.setAvatar(getAvatar());
        toDisk.setTheme(getTheme());
        toDisk.setServerURL(getServerURL());
        toDisk.save();
    }
    // ────────────────────────────────────────────────────────

    // ─── Username ───────────────────────────────────────────
    public static void setUsername(String username) {
        if (!validUsername(username)) {
            throw new IllegalArgumentException("Invalid username");
        }
        getInstance().username = username;
    }

    public static String getUsername() {
        return getInstance().username;
    }
    // ────────────────────────────────────────────────────────

    // ─── Avatar ─────────────────────────────────────────────
    public static void setAvatar(String avatar) {
        if (!validAvatar(avatar)) {
            throw new IllegalArgumentException("Invalid avatar");
        }
        getInstance().avatar = avatar;
    }

    public static String getAvatar() {
        return getInstance().avatar;
    }
    // ────────────────────────────────────────────────────────

    // ─── Theme ──────────────────────────────────────────────
    public static void setTheme(String theme) {
        if (!validTheme(theme)) {
            return;
        }
        AssetManager.getThemeManager().loadTheme(theme);
        getInstance().theme = theme;
    }

    public static String getTheme() {
        return getInstance().theme;
    }
    // ────────────────────────────────────────────────────────

    // ─── Server URL ─────────────────────────────────────────
    public static void setServerURL(String serverURL) {
        if (!validServerURL(serverURL)) {
            throw new IllegalArgumentException("Invalid server URL");
        }
        getInstance().serverURL = serverURL;
    }

    public static String getServerURL() {
        return getInstance().serverURL;
    }
    // ────────────────────────────────────────────────────────

    // ─── Validation Methods ─────────────────────────────────

    public static boolean validUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.length() <= 16;
    }

    public static boolean validAvatar(String avatar) {
        return AssetManager.getAvatars().containsKey(avatar);
    }

    public static boolean validTheme(String theme) {
        return AssetManager.getThemeManager().getThemes().contains(theme);
    }

    // Regex for matching an IPv4 address (0.0.0.0 – 255.255.255.255)
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4]\\d|[01]?\\d?\\d)\\.){3}"
                    + "(25[0-5]|2[0-4]\\d|[01]?\\d?\\d)$"
    );

    // Helper: validate IPv4 via regex
    private static boolean isValidIPv4(String ip) {
        Matcher m = IPV4_PATTERN.matcher(ip);
        return m.matches();
    }

    // Validate: IPv4 address using websocket protocol
    public static boolean validServerURL(String url) {
        if (url == null) {
            return false;
        }

        URI uri;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            // malformed URI
            return false;
        }

        // 1) must use ws://
        if (!"ws".equalsIgnoreCase(uri.getScheme())) {
            return false;
        }

        // 2) must have a valid host: either IPv4 or "localhost"
        String host = uri.getHost();
        if (host == null || (!isValidIPv4(host) && !"localhost".equalsIgnoreCase(host))) {
            return false;
        }

        // 3) must specify a port in [1, 65535]
        int port = uri.getPort();
        return port >= 1 && port <= 65_535;
    }
}

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

    // ─── Defaults ────────────────────────────────────────
    private static final String DEFAULT_USERNAME   = "Guest";
    private static final String DEFAULT_AVATAR     = "default";
    private static final String DEFAULT_THEME      = AssetManager.getInstance().getThemeManager().getDefaultTheme();
    private static final String DEFAULT_SERVER_URL = "ws://localhost:8080";
    // ───────────────────────────────────────────────────────────

    private String username;
    private String avatar;
    private String theme;
    private String serverURL;

    private SettingsManager() {
        // 1) attempt to load from disk
        SettingsJsonHandler fromDisk;
        try {
            fromDisk = SettingsJsonHandler.load();
        } catch (UncheckedIOException e) {
            // I/O problem reading file → just use defaults
            fromDisk = new SettingsJsonHandler();
        }

        // 2) apply loaded values or fall back to defaults
        setUsername(fromDisk.getUsername()  != null
                ? fromDisk.getUsername()
                : DEFAULT_USERNAME);
        setAvatar(fromDisk.getAvatar()    != null
                ? fromDisk.getAvatar()
                : DEFAULT_AVATAR);
        setTheme(fromDisk.getTheme()     != null
                ? fromDisk.getTheme()
                : DEFAULT_THEME);
        setServerURL(fromDisk.getServerURL() != null
                ? fromDisk.getServerURL()
                : DEFAULT_SERVER_URL);
    }

    public static SettingsManager getInstance() {
        if (instance == null) {
            instance = new SettingsManager();
        }
        return instance;
    }

    // ─── Public API ────────────────────────────────────────────
    public void setUsername(String username) {
        if (!validUsername(username)) {
            throw new IllegalArgumentException("Invalid username");
        }
        this.username = username;
    }
    public String getUsername() {
        return username;
    }

    public void setAvatar(String avatar) {
        if (!validAvatar(avatar)) {
            throw new IllegalArgumentException("Invalid avatar");
        }
        this.avatar = avatar;
    }
    public String getAvatar() {
        return avatar;
    }

    public void setTheme(String theme) {
        if (!validTheme(theme)) {
            return;
        }
        AssetManager.getInstance().getThemeManager().loadTheme(theme);
        this.theme = theme;
    }
    public String getTheme() {
        return theme;
    }

    public void setServerURL(String serverURL) {
        if (!validServerURL(serverURL)) {
            throw new IllegalArgumentException("Invalid server URL");
        }
        this.serverURL = serverURL;
    }
    public String getServerURL() {
        return serverURL;
    }

    /** Push the current in‑memory settings back out to disk. */
    public void save() {
        SettingsJsonHandler toDisk = new SettingsJsonHandler();
        toDisk.setUsername(this.username);
        toDisk.setAvatar(this.avatar);
        toDisk.setTheme(this.theme);
        toDisk.setServerURL(this.serverURL);
        toDisk.save();
    }

    // ─── validation methods (as you already have them) ──────────

    public boolean validUsername(String username) {
        if (username == null || username.isEmpty()) {
            return false;
        }
        return username.length() <= 16;
    }

    public boolean validAvatar(String avatar) {
        return AssetManager.getInstance().getAvatars().containsKey(avatar);
    }

    public boolean validTheme(String theme) {
        return AssetManager.getInstance().getThemeManager().getAvailableThemes().contains(theme);
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

    /**
     * Returns true if the input is a valid WebSocket server URL of the form:
     *   ws://<IPv4-address>:<port>
     * where the IPv4 address is in 0–255 range per octet and port is 1–65535.
     */
    public boolean validServerURL(String url) {
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

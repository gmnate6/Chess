package frontend.model.assets;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CursorManager {
    private final Map<String, Cursor> cursors = new HashMap<>();
    int size = 32;

    private void loadCursor(String key, String path) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor grabCursor = toolkit.createCustomCursor(ImageManager.renderSvgImage(path, size, size), new Point(size/2, size/2), key);
        cursors.put(key, grabCursor);
    }

    public void loadCursors() {
        loadCursor("grab", "cursors/grab.svg");
        loadCursor("grabbing", "cursors/grabbing.svg");
    }

    public Cursor getCursor(String key) {
        return cursors.get(key);
    }
}

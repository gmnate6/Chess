package com.nathanholmberg.chess.client.model.assets;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class CursorManager {
    private final Map<String, Cursor> cursors;
    int size = 32;

    public CursorManager() {
        cursors = new HashMap<>();
        loadCursors();
    }

    private void loadCursor(String key, String path, boolean centered) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Point hotSpot = centered ? new Point(size/2, size/2) : new Point(0, 0);
        Cursor grabCursor = toolkit.createCustomCursor(ImageManager.renderSvgImage(path, size, size), hotSpot, key);
        cursors.put(key, grabCursor);
    }

    public void loadCursors() {
        loadCursor("grab", "cursors/grab.svg", true);
        loadCursor("grabbing", "cursors/grabbing.svg", true);
    }

    public Cursor getCursor(String key) {
        return cursors.get(key);
    }
}

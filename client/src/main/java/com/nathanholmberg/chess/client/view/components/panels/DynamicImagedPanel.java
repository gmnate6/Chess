package com.nathanholmberg.chess.client.view.components.panels;

import java.awt.*;

public class DynamicImagedPanel extends AbstractImagedPanel {
    public DynamicImagedPanel(Image image) {
        super();
        setImage(image);
    }
    public DynamicImagedPanel() {
        super();
    }

    protected Point getNewPoint() {
        return new Point(0, 0);
    }

    protected Dimension getNewDimension() {
        return new Dimension(getWidth(), getHeight());
    }
}

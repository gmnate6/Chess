package frontend.view.components.panels;

import javax.swing.*;
import java.awt.*;

public class DynamicImagedPanel extends JPanel {
    private Image originalImage;
    private Image cachedImage = null;
    private int cachedWidth = -1;
    private int cachedHeight = -1;

    public DynamicImagedPanel(Image image) {
        super();
        this.originalImage = image;
        setOpaque(false);
        this.repaint();
    }
    public DynamicImagedPanel() {
        this(null);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) { return; }
        if (originalImage == null) { return; }

        // Update cachedImage if size changed
        if (hasChangedSize()) {
            cachedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            cachedWidth = width;
            cachedHeight = height;
        }
        g.drawImage(cachedImage, 0, 0, width, height, this);
    }

    protected boolean hasChangedSize() {
        int width = getWidth();
        int height = getHeight();
        return (cachedImage == null || width != cachedWidth || height != cachedHeight);
    }

    public void setImage(Image image) {
        this.originalImage = image;
        this.repaint();
    }
}

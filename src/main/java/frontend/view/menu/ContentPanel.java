package frontend.view.menu;

import javax.swing.*;

public class ContentPanel extends JPanel {
    protected final TransparentPanel transparentPanel;
    protected float ratio;
    protected float heightRatio = 0.7f;

    public ContentPanel(float ratio) {
        setOpaque(false);
        setLayout(null);
        this.ratio = ratio;

        // Create TransparentPanel with an initial size
        transparentPanel = new TransparentPanel();
        add(transparentPanel);

        // Add a ComponentListener to adjust TransparentPanel when TitlePanel resizes
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                onResize();
            }
        });
    }

    private void onResize() {
        // Calculate the size of TransparentPanel
        int height = (int) (getHeight() * heightRatio);
        int width = (int) (height * ratio);
        height = Math.max(height, 50);
        width = Math.max(width, 50);

        // Calculate the position to center the TransparentPanel
        int x = (getWidth() - width) / 2;
        int y = (getHeight() - height) / 2;

        // Update TransparentPanel's bounds
        transparentPanel.setBounds(x, y, width, height);
        revalidate();
        repaint();
    }
}

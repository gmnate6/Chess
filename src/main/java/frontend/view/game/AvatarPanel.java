package frontend.view.game;

import frontend.view.utils.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class AvatarPanel extends JPanel {
    private BufferedImage image;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw red if no image
        if (image == null) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
            return;
        }

        // Draw Image
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        repaint();
    }
}

package frontend.view.game;

import frontend.view.utils.ImageLoader;

import frontend.view.utils.PieceImageLoader;
import utils.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SquareButton extends JButton {
    private final Color color;
    private PieceImageLoader pieceImageLoader;

    // State
    private BufferedImage pieceImage = null;
    private boolean isHinted = false;
    private boolean isHighLighted = false;
    private boolean isMarkedRed = false;

    public SquareButton(Color color, PieceImageLoader pieceImageLoader) {
        this.color = color;
        this.pieceImageLoader = pieceImageLoader;

        // Setup
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);

        // Ensures the button repaints when resized
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                buildIcon();  // Forces a repaint to adjust the image size
            }
        });
    }

    // Builds and sets icon
    public void buildIcon() {
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) { return; }

        // Create a new buffered image for the icon
        BufferedImage iconImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = iconImage.createGraphics();

        // Square
        g.drawImage(color == Color.WHITE ? pieceImageLoader.getWhiteImage() : pieceImageLoader.getBlackImage(), 0, 0, width, height, this);

        // Highlight
        if (isHighLighted) {
            g.drawImage(pieceImageLoader.getHighlightImage(), 0, 0, width, height, this);
        }

        // Marked Red
        if (isMarkedRed) {
            g.drawImage(pieceImageLoader.getMarkedRedImage(), 0, 0, width, height, this);
        }

        // Draw Piece
        if (pieceImage != null) {
            Image scaledImage = pieceImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            g.drawImage(scaledImage, 0, 0, width, height, this);
        }

        // Hint
        if (isHinted) {
            g.drawImage(pieceImage == null ? pieceImageLoader.getMoveHintImage() : pieceImageLoader.getCaptureHintImage(), 0, 0, width, height, this);
        }

        // Set the button's icon to the rendered image
        g.dispose();
        setIcon(new ImageIcon(iconImage));

    }

    // Clear Overlays
    public void clearOverlays() {
        // if (!isHinted  && !isHighLighted && !isMarkedRed) { return; }
        isHinted = false;
        isHighLighted = false;
        isMarkedRed = false;
        buildIcon();
    }

    // Setters
    public void setPiece(Character pieceChar) {
        if (pieceChar == null) {
            pieceImage = null;
            return;
        }
        pieceImage = pieceImageLoader.getPieceImage(pieceChar);
    }
    public void setHint(boolean isHinted) {
        this.isHinted = isHinted;
        buildIcon();
    }
    public void setHighLight(boolean isHighLighted) {
        this.isHighLighted = isHighLighted;
        this.isMarkedRed = false;
        buildIcon();
    }
    public void setMarkedRed(boolean isMarkedRed) {
        this.isMarkedRed = isMarkedRed;
        this.isHighLighted = false;
        buildIcon();
    }
}

package frontend.view.game;

import frontend.view.utils.ImageLoader;

import utils.Color;

import javax.swing.*;
import java.awt.*;

public class SquareButton extends JButton {
    private final Color color;
    private Character currentPiece = null;
    private boolean isHighLighted = false;
    private boolean isHinted = false;
    public static int SIZE = 70;

    public SquareButton(Color color) {
        this.color = color;
        buildIcon();

        // Setup
        setPreferredSize(new Dimension(SIZE, SIZE));
        setBorderPainted(false);
        setFocusPainted(false);
        setContentAreaFilled(false);
    }

    // Build and Set Icon based on Button State
    private void buildIcon() {
        // Base Color Icon
        ImageIcon icon = ImageLoader.getImageIcon("board/" + (color == Color.WHITE ? "white.png": "black.png"));

        // Active Icon
        if (isHighLighted) {
            ImageIcon activeIcon = ImageLoader.getImageIcon("board/high_light.png");
            icon = ImageLoader.overlayIcons(icon, activeIcon);
        }

        // Piece Icon
        if (currentPiece != null) {
            ImageIcon pieceIcon = ImageLoader.getPieceIcon(currentPiece);
            icon = ImageLoader.overlayIcons(icon, pieceIcon);
        }

        // Hint Icon
        if (isHinted) {
            ImageIcon hintIcon = ImageLoader.getImageIcon("board/" + (currentPiece == null ? "move_hint.png": "capture_hint.png"));
            icon = ImageLoader.overlayIcons(icon, hintIcon);
        }

        // Scale Image
        Image scaledImage = icon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH);
        // Set Icon
        setIcon(new ImageIcon(scaledImage));
    }

    // Clear Overlays
    public void clearOverlays() {
        if (!isHighLighted && !isHinted) {
            return;
        }
        isHighLighted = false;
        isHinted = false;
        buildIcon();
    }

    // Setters
    public void setPiece(Character pieceChar) {
        if (this.currentPiece == pieceChar) {
            return;
        }
        currentPiece = pieceChar;
        buildIcon();
    }
    public void setHighLight(boolean isHighLighted) {
        if (this.isHighLighted == isHighLighted) {
            return;
        }
        this.isHighLighted = isHighLighted;
        buildIcon();
    }
    public void setHint(boolean isHinted) {
        if (this.isHinted == isHinted) {
            return;
        }
        this.isHinted = isHinted;
        buildIcon();
    }
}

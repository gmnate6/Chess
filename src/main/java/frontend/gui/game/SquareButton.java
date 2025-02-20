package frontend.gui.game;

import frontend.gui.utils.ImageLoader;
import frontend.Color;

import javax.swing.*;
import java.awt.*;

public class SquareButton extends JButton {
    private final Color color;
    private Character currentPiece = null;
    private boolean isActive = false;
    private boolean isHint = false;
    public static int SIZE = 80;

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
        if (isActive) {
            ImageIcon activeIcon = ImageLoader.getImageIcon("board/active.png");
            icon = ImageLoader.overlayIcons(icon, activeIcon);
        }

        // Piece Icon
        if (currentPiece != null) {
            ImageIcon pieceIcon = ImageLoader.getPieceIcon(currentPiece);
            icon = ImageLoader.overlayIcons(icon, pieceIcon);
        }

        // Hint Icon
        if (isHint) {
            ImageIcon hintIcon = ImageLoader.getImageIcon("board/" + (currentPiece == null ? "move_hint.png": "capture_hint.png"));
            icon = ImageLoader.overlayIcons(icon, hintIcon);
        }

        // Scale Image
        Image scaledImage = icon.getImage().getScaledInstance(SIZE, SIZE, Image.SCALE_SMOOTH);
        // Set Icon
        setIcon(new ImageIcon(scaledImage));
    }

    public void setPiece(Character pieceChar) {
        currentPiece = pieceChar;
        buildIcon();
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
        buildIcon();
    }
    public void setHint(boolean isHint) {
        this.isHint = isHint;
        buildIcon();
    }
}

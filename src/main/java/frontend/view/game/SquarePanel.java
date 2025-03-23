package frontend.view.game;

import frontend.view.utils.AssetManager;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SquarePanel extends JPanel {
    private final AssetManager assetManager = AssetManager.getInstance();

    // State
    private BufferedImage pieceImage = null;
    private boolean isHinted = false;
    private boolean isHighlighted = false;
    private boolean isMarkedRed = false;

    public SquarePanel() {
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawSquare(g);
    }

    private void drawSquare(Graphics g) {
        int width = getWidth();
        int height = getHeight();
        if (width == 0 || height == 0) { return; }

        // Highlight
        if (isHighlighted) {
            g.setColor(assetManager.getColor("highlight"));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Marked Red
        if (isMarkedRed) {
            g.setColor(assetManager.getColor("markedRed"));
            g.fillRect(0, 0, getWidth(), getHeight());
        }

        // Draw Piece
        if (pieceImage != null) {
            g.drawImage(pieceImage, 0, 0, width, height, this);
        }

        // Hint
        if (isHinted) {
            g.drawImage(pieceImage == null ? assetManager.getImage("hint") : assetManager.getImage("capture_hint"), 0, 0, width, height, this);
        }
    }

    public void clearOverlays() {
        if (!isHinted  && !isHighlighted && !isMarkedRed) { return; }
        isHinted = false;
        isHighlighted = false;
        isMarkedRed = false;
        repaint();
    }

    public void setPiece(Character pieceChar) {
        if (pieceChar == null) {
            pieceImage = null;
            return;
        }
        pieceImage = assetManager.getImage(pieceChar.toString());
    }

    public void setHint(boolean isHinted) {
        this.isHinted = isHinted;
        repaint();
    }

    public void setHighLight(boolean isHighLighted) {
        this.isHighlighted = isHighLighted;
        this.isMarkedRed = false;
        repaint();
    }

    public void setMarkedRed(boolean isMarkedRed) {
        this.isMarkedRed = isMarkedRed;
        this.isHighlighted = false;
        repaint();
    }
}

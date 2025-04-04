package frontend.view.game;

import engine.exceptions.IllegalPositionException;
import engine.game.Game;
import engine.types.Move;
import engine.types.Position;
import engine.pieces.Piece;

import frontend.view.utils.AssetManager;
import frontend.view.utils.DynamicImagedPanel;
import utils.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class BoardPanel extends DynamicImagedPanel {
    private final Square[][] squares = new Square[SIZE][SIZE];
    public static int SIZE = 8;
    private Color perspective;
    private Move lastMove;

    public BoardPanel() {
        super(AssetManager.getInstance().getImage("board"));

        // Setup
        setBackground(new java.awt.Color(0xE5E5E5));
        setPerspective(Color.WHITE);

        // Initialize Squares
        for (int rank = SIZE - 1; rank >= 0; rank--) {
            for (int file = 0; file < SIZE; file++) {
                squares[file][rank] = new Square();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw Board
        super.paintComponent(g);

        // Paint Squares
        paintSquares(g);
    }

    public void paintSquares(Graphics g) {
        int squareWidth = getWidth() / SIZE;
        int squareHeight = getHeight() / SIZE;

        // Draw Piece / Overlays
        for (int rank = 0; rank < SIZE; rank++) {
            for (int file = 0; file < SIZE; file++) {
                AssetManager assetManager = AssetManager.getInstance();
                Position position = new Position(file, rank);
                Point point = positionToPoint(position);
                Square square = getSquare(position);

                // Highlight
                if (square.isHighlighted()) {
                    g.setColor(assetManager.getColor("highlight"));
                    g.fillRect(point.x, point.y, squareWidth, squareHeight);
                }

                // Marked Red
                if (square.isMarkedRed()) {
                    g.setColor(assetManager.getColor("markedRed"));
                    g.fillRect(point.x, point.y, squareWidth, squareHeight);
                }

                // Draw Piece
                if (square.getPiece() != null) {
                    BufferedImage pieceImage = assetManager.getImage(square.getPiece());
                    g.drawImage(pieceImage, point.x, point.y, squareWidth, squareHeight, this);
                }

                // Hint
                if (square.isHinted()) {
                    g.drawImage(square.getPiece() == null ?
                            assetManager.getImage("hint") :
                            assetManager.getImage("capture_hint"),
                            point.x, point.y, squareWidth, squareHeight, this);
                }
            }
        }
    }

    public void setPerspective(Color perspective) {
        if (perspective == null) { throw new NullPointerException("Perspective cannot be null."); }
        this.perspective = perspective;
    }

    public Square getSquare(Position position) {
        if (perspective == Color.BLACK) {
            position = position.inverse();
        }
        return squares[position.file()][position.rank()];
    }

    public void loadPieces(Game game) {
        for (int file = 0; file < SIZE; file++) {
            for (int rank = 0; rank < SIZE; rank++) {
                Position position = new Position(file, rank);
                Piece currentPiece = game.board.getPieceAt(position);

                // Set Current Piece Char
                Character currentPieceChar = null;
                if (currentPiece != null) {
                    currentPieceChar = currentPiece.toChar();
                }

                // Get Square
                Square square = getSquare(position);

                // Set Piece
                square.setPiece(currentPieceChar);

                // Clear Layovers
                square.clearOverlays();
            }
        }

        // Set Last Move
        setLastMove(game.getMoveHistory().getLastMove());
        repaint();
    }

    public Position pointToPosition(Point point) {
        int squareWidth = getWidth() / SIZE;
        int squareHeight = getHeight() / SIZE;

        // Get File and Rank
        int min = 0;
        int max = SIZE - 1;
        int file = Math.max(min, Math.min(max, point.x / squareWidth));
        int rank = Math.max(min, Math.min(max, max - (point.y / squareHeight)));

        // Get Position
        try {
            Position pos = new Position(file, rank);

            // Inverse Position if black's perspective
            if (perspective == Color.WHITE) { return pos; }
            return pos.inverse();
        } catch (IllegalPositionException e) {
            System.err.println("Error: Point outside of board.");
        }
        return null;
    }

    public Point positionToPoint(Position position) {
        float squareWidth = (float) getWidth() / (float) SIZE;
        float squareHeight = (float) getHeight() / (float) SIZE;

        // Inverse Position if black's perspective
        if (perspective == Color.BLACK) { position = position.inverse(); }

        // Get Point
        int x = (int) (position.file() * squareWidth);
        int y = (int) (((SIZE - 1) - position.rank()) * squareHeight);

        return new Point(x, y);
    }

    public void setLastMove(Move move) {
        // Remove Old Last Move
        if (lastMove != null) {
            Square initialSquare = getSquare(lastMove.initialPosition());
            Square finalSquare = getSquare(lastMove.finalPosition());
            initialSquare.setHighLight(false);
            finalSquare.setHighLight(false);
        }

        // Add New Last Move
        lastMove = move;
        if (move == null) { return; }
        Square newInitialSquare = getSquare(move.initialPosition());
        Square newFinalSquare = getSquare(move.finalPosition());
        newInitialSquare.setHighLight(true);
        newFinalSquare.setHighLight(true);
        repaint();
    }

    public void setHighlight(Position position, boolean isHighlighted) {
        Square square = getSquare(position);
        if (square != null) {
            square.setHighLight(isHighlighted);
        }
        repaint();
    }

    public void setHint(Position position, boolean isHinted) {
        Square square = getSquare(position);
        if (square != null) {
            square.setHint(isHinted);
        }
        repaint();
    }

    public void clearHints() {
        for (int file = 0; file < SIZE; file++) {
            for (int rank = 0; rank < SIZE; rank++) {
                setHint(new Position(file, rank), false);
            }
        }
        repaint();
    }














    public void createJFrame() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess - BoardPanel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(700, 700));
            frame.setLayout(new BorderLayout());
            //frame.setResizable(false);

            // Add the BoardPanel to the frame
            frame.add(this, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}

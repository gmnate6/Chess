package frontend.view.game;

import engine.exceptions.IllegalPositionException;
import engine.game.Game;
import engine.types.Move;
import engine.types.Position;
import engine.pieces.Piece;

import frontend.model.assets.AssetManager;
import frontend.view.utils.DynamicImagedPanel;
import utils.Color;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class BoardPanel extends DynamicImagedPanel {
    private final Square[][] squares = new Square[SIZE][SIZE];
    public final static int SIZE = 8;
    private Color perspective;
    private Move lastMove;

    // Dragging Pieces
    private Position pickedUpPosition;
    private Point mousePosition;

    public BoardPanel() {
        super(AssetManager.getInstance().getThemeImage("board"));

        // Setup
        setBackground(new java.awt.Color(0xE5E5E5));
        setPerspective(Color.WHITE);

        // Initialize Squares
        for (int rank = SIZE - 1; rank >= 0; rank--) {
            for (int file = 0; file < SIZE; file++) {
                squares[file][rank] = new Square();
            }
        }

        // Mouse Listener
        addMouseMotionListener(new MouseInputAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                onMouseInteraction(e.getPoint());
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                onMouseInteraction(e.getPoint());
            }
        });
    }

    private void onMouseInteraction(Point point) {
        // Update Cursor
        updateCursor(point);

        // Update positions when not holding piece
        if (pickedUpPosition == null) {
            mousePosition = point;
            return;
        }

        int squareWidth = getWidth() / SIZE;
        int squareHeight = getHeight() / SIZE;

        // Draw over old picked up piece
        int x = mousePosition.x - (squareWidth / 2);
        int y = mousePosition.y - (squareHeight / 2);
        repaint(new Rectangle(x, y, squareWidth, squareHeight));

        // Update Mouse Position
        mousePosition = point;

        // Draw new picked up piece
        x = mousePosition.x - (squareWidth / 2);
        y = mousePosition.y - (squareHeight / 2);
        repaint(new Rectangle(x, y, squareWidth, squareHeight));
    }

    private void updateCursor(Point point) {
        if (point == null) { return; }
        Square square = getSquare(pointToPosition(point));

        // Closed Grab
        if (pickedUpPosition != null) {
            setCursor(AssetManager.getInstance().getCursor("grabbing"));
            return;
        }

        // Open Grab
        if (square.getPiece() != null ) {
            setCursor(AssetManager.getInstance().getCursor("grab"));
            return;
        }

        // Normal
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw Board
        super.paintComponent(g);

        // Paint Squares
        paintPieces(g);

        // Draw PickedUpPiece
        paintPickedUpPiece(g);
    }

    private void paintPickedUpPiece(Graphics g) {
        if (pickedUpPosition == null || mousePosition == null) { return; }

        // Get Some stuff
        int squareWidth = getWidth() / SIZE;
        int squareHeight = getHeight() / SIZE;
        AssetManager assetManager = AssetManager.getInstance();
        Square square = getSquare(pickedUpPosition);
        BufferedImage pieceImage = assetManager.getThemeImage(square.getPiece());

        // Draw piece at mouse
        int x = mousePosition.x - (squareWidth / 2);
        int y = mousePosition.y - (squareHeight / 2);
        g.drawImage(pieceImage, x, y, squareWidth, squareHeight, this);
    }

    private void paintPieces(Graphics g) {
        int squareWidth = getWidth() / SIZE;
        int squareHeight = getHeight() / SIZE;
        squareWidth += 2; squareHeight += 2;
        AssetManager assetManager = AssetManager.getInstance();

        // Draw Piece / Overlays
        for (int rank = 0; rank < SIZE; rank++) {
            for (int file = 0; file < SIZE; file++) {
                Position position = new Position(file, rank);
                Point point = positionToPoint(position);
                point.x--; point.y--;
                Square square = getSquare(position);

                // Highlight
                boolean isLastMove = lastMove != null && (lastMove.initialPosition().equals(position) || lastMove.finalPosition().equals(position));
                if (square.isHighlighted() || isLastMove) {
                    g.setColor(assetManager.getThemeColor("highlight"));
                    g.fillRect(point.x, point.y, squareWidth, squareHeight);
                }

                // Draw Piece
                if (square.getPiece() != null) {
                    BufferedImage pieceImage = assetManager.getThemeImage(square.getPiece());

                    // Skip PickUpPiece
                    if (!position.equals(pickedUpPosition)) {
                        g.drawImage(pieceImage, point.x, point.y, squareWidth, squareHeight, this);
                    }
                }

                // Marked Red
                if (square.isMarkedRed()) {
                    g.setColor(assetManager.getThemeColor("markedRed"));
                    g.fillRect(point.x, point.y, squareWidth, squareHeight);
                }

                // Hint
                if (square.isHinted()) {
                    g.drawImage(square.getPiece() == null ?
                                    assetManager.getThemeImage("hint") :
                                    assetManager.getThemeImage("capture_hint"),
                            point.x, point.y, squareWidth, squareHeight, this);
                }
            }
        }
    }

    public void setPerspective(Color perspective) {
        if (perspective == null) { throw new NullPointerException("Perspective cannot be null."); }
        this.perspective = perspective;
        repaint();
    }

    public Square getSquare(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Position is null.");
        }

        // Get Square
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

        // Update Cursor
        updateCursor(mousePosition);
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

    private Point positionToPoint(Position position) {
        float squareWidth = (float) getWidth() / (float) SIZE;
        float squareHeight = (float) getHeight() / (float) SIZE;

        // Inverse Position if black's perspective
        if (perspective == Color.BLACK) { position = position.inverse(); }

        // Get Point
        int x = Math.round(position.file() * squareWidth);
        int y = Math.round(((SIZE - 1) - position.rank()) * squareHeight);
        return new Point(x, y);
    }

    public void setLastMove(Move move) {
        lastMove = move;
        repaint();
    }

    public void setHighlight(Position position, boolean isHighlighted) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Position is null.");
        }
        Square square = getSquare(position);
        if (square != null) {
            square.setHighLight(isHighlighted);
        }
        repaint();
    }

    public void setHint(Position position, boolean isHinted) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Position is null.");
        }
        Square square = getSquare(position);
        if (square != null) {
            square.setHint(isHinted);
        }
        repaint();
    }

    public void setMarkedRed(Position position, boolean isMarkedRed) {
        if (position == null) {
            throw new IllegalArgumentException("Error: Position is null.");
        }
        Square square = getSquare(position);
        if (square != null) {
            square.setMarkedRed(isMarkedRed);
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

    public void clearMarkedRed() {
        for (int file = 0; file < SIZE; file++) {
            for (int rank = 0; rank < SIZE; rank++) {
                setMarkedRed(new Position(file, rank), false);
            }
        }
    }

    public void grabPiece(Position position) {
        if (position == null) { return; }
        pickedUpPosition = position;
        updateCursor(mousePosition);
        repaint();
    }

    public void dropPiece() {
        pickedUpPosition = null;
        updateCursor(mousePosition);
        repaint();
    }
}

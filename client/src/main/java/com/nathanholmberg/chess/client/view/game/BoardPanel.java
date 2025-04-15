package com.nathanholmberg.chess.client.view.game;

import com.nathanholmberg.chess.client.model.assets.AssetManager;
import com.nathanholmberg.chess.client.view.components.panels.DynamicImagedPanel;
import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.exceptions.IllegalPositionException;
import com.nathanholmberg.chess.engine.game.Game;
import com.nathanholmberg.chess.engine.pieces.Piece;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;

import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class BoardPanel extends DynamicImagedPanel {
    private final Square[][] squares = new Square[SIZE][SIZE];
    public final static int SIZE = 8;
    private Color perspective;
    private Move lastMove;

    // Dragging Pieces
    private Position pickedUpPosition;
    private Point mousePosition;

    public BoardPanel() {
        super(AssetManager.getThemeImage("board"));

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
            setCursor(AssetManager.getCursor("grabbing"));
            return;
        }

        // Open Grab
        if (square.getPiece() != null ) {
            setCursor(AssetManager.getCursor("grab"));
            return;
        }

        // Normal
        setCursor(Cursor.getDefaultCursor());
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Draw Board
        super.paintComponent(g);

        // Paint Coordinates
        paintCoordinates(g);

        // Paint Pieces
        paintPieces(g);

        // Draw PickedUpPiece
        paintPickedUpPiece(g);
    }

    private void paintPieces(Graphics g) {
        int squareWidth = getWidth() / SIZE;
        int squareHeight = getHeight() / SIZE;
        squareWidth += 2; squareHeight += 2;

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
                    g.setColor(AssetManager.getThemeColor("highlight"));
                    g.fillRect(point.x, point.y, squareWidth, squareHeight);
                }

                // Draw Piece
                if (square.getPiece() != null) {
                    BufferedImage pieceImage = AssetManager.getThemeImage(square.getPiece());

                    // Skip PickUpPiece
                    if (!position.equals(pickedUpPosition)) {
                        g.drawImage(pieceImage, point.x, point.y, squareWidth, squareHeight, this);
                    }
                }

                // Marked Red
                if (square.isMarkedRed()) {
                    g.setColor(AssetManager.getThemeColor("markedRed"));
                    g.fillRect(point.x, point.y, squareWidth, squareHeight);
                }

                // Hint
                if (square.isHinted()) {
                    g.drawImage(square.getPiece() == null ?
                                    AssetManager.getThemeImage("hint") :
                                    AssetManager.getThemeImage("capture_hint"),
                            point.x, point.y, squareWidth, squareHeight, this);
                }
            }
        }
    }

    private void paintCoordinates(Graphics g) {
        int squareWidth = getWidth() / SIZE;
        int squareHeight = getHeight() / SIZE;

        int fontSize = (int) (squareHeight * .15);
        int offset = (int) (squareHeight * 0.04);
        Font font = AssetManager.getFont("chess_font", fontSize);
        g.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);

        // Paint Ranks
        for (int i = 1; i <= SIZE; i++) {
            String rank = "" + i;

            // Calc Position
            int y;
            if (perspective == Color.WHITE) {
                y = getHeight() - i * squareHeight + offset + metrics.getAscent();
            } else {
                y = (i-1) * squareHeight + offset + metrics.getAscent();
            }

            // Set Color
            if (perspective == Color.WHITE) {
                g.setColor(
                        i % 2 == 0 ?
                                AssetManager.getThemeColor("boardBlack") :
                                AssetManager.getThemeColor("boardWhite")
                );
            } else {
                g.setColor(
                        i % 2 == 0 ?
                                AssetManager.getThemeColor("boardWhite") :
                                AssetManager.getThemeColor("boardBlack")
                );
            }

            // Paint Rank
            g.drawString(rank, offset, y);
        }

        // Paint File
        for (int i = 1; i <= SIZE; i++) {
            String file = String.valueOf((char) ('a' + (i-1)));

            // Calc Position
            int x;
            int y = getHeight() - offset;
            if (perspective == Color.WHITE) {
                x = i * squareWidth - offset - metrics.stringWidth(file);
            } else {
                x = getWidth() - (i-1) * squareWidth - offset - metrics.stringWidth(file);
            }

            // Set Color
            if (perspective == Color.WHITE) {
                g.setColor(
                        i % 2 == 0 ?
                                AssetManager.getThemeColor("boardBlack") :
                                AssetManager.getThemeColor("boardWhite")
                );
            } else {
                g.setColor(
                        i % 2 == 0 ?
                                AssetManager.getThemeColor("boardWhite") :
                                AssetManager.getThemeColor("boardBlack")
                );
            }

            // Paint Rank
            g.drawString(file, x, y);
        }
    }

    private void paintPickedUpPiece(Graphics g) {
        if (pickedUpPosition == null || mousePosition == null) { return; }

        // Get Some stuff
        int squareWidth = getWidth() / SIZE;
        int squareHeight = getHeight() / SIZE;
        Square square = getSquare(pickedUpPosition);
        if (square.getPiece() == null) { return; }
        BufferedImage pieceImage = AssetManager.getThemeImage(square.getPiece());

        // Draw piece at mouse
        int x = mousePosition.x - (squareWidth / 2);
        int y = mousePosition.y - (squareHeight / 2);
        g.drawImage(pieceImage, x, y, squareWidth, squareHeight, this);
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

    public CompletableFuture<Character> promptPromotion(Position position, Color color) {
        CompletableFuture<Character> future = new CompletableFuture<>();
        boolean rightSideUp = positionToPoint(position).y == 0;

        AtomicReference<PromotionPanel> panelRef = new AtomicReference<>();
        AtomicReference<ComponentAdapter> adapterRef = new AtomicReference<>();

        panelRef.set(new PromotionPanel(color, rightSideUp, chosen -> {
            future.complete(chosen);
            remove(panelRef.get());
            removeComponentListener(adapterRef.get());
            repaint();
        }));

        Runnable setBounds = () -> {
            PromotionPanel panel = panelRef.get();
            panel.setSquareSize(getWidth() / SIZE);
            panel.revalidate();
            panel.repaint();
            Rectangle bounds = new Rectangle(positionToPoint(position), panel.getPreferredSize());
            if (bounds.y + panel.getPreferredSize().height > getHeight()) {
                bounds.y -= panel.getPreferredSize().height - getWidth() / SIZE;
            }
            panel.setBounds(bounds);
        };

        setBounds.run();

        ComponentAdapter adapter = new ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                setBounds.run();
            }
        };
        adapterRef.set(adapter);
        addComponentListener(adapter);

        add(panelRef.get());
        revalidate();
        repaint();

        return future;
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

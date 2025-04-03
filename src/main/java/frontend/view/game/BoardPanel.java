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

public class BoardPanel extends DynamicImagedPanel {
    private final SquarePanel[][] squares = new SquarePanel[SIZE][SIZE];
    public static int SIZE = 8;
    private Color perspective;
    private Move lastMove;

    public BoardPanel() {
        super(AssetManager.getInstance().getImage("board"));

        // Setup
        setLayout(new GridLayout(SIZE, SIZE, 0, 0));
        setBackground(new java.awt.Color(0xE5E5E5));
        setPerspective(Color.WHITE);
    }

    public void setPerspective(Color perspective) {
        if (perspective == null) { throw new NullPointerException("Perspective cannot be null."); }

        // Update Perspective
        this.perspective = perspective;

        // Remove Old Squares
        removeAll();

        // Initialize Squares
        if (perspective == Color.WHITE) {
            // White
            for (int rank = SIZE - 1; rank >= 0; rank--) {
                for (int file = 0; file < SIZE; file++) {
                    SquarePanel square = new SquarePanel();
                    squares[file][rank] = square;
                    add(square);
                }
            }
        } else if (perspective == Color.BLACK) {
            // Black
            for (int rank = 0; rank < SIZE; rank++) {
                for (int file = SIZE - 1; file >= 0; file--) {
                    SquarePanel square = new SquarePanel();
                    squares[file][rank] = square;
                    add(square);
                }
            }
        }
    }

    public SquarePanel getSquare(Position position) {
        return squares[position.file()][position.rank()];
    }

    public void loadPieces(Game game) {
        // Load Board
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
                SquarePanel square = getSquare(position);

                // Set Piece
                square.setPiece(currentPieceChar);

                // Clear Layovers
                square.clearOverlays();
            }
        }

        // Set Last Move
        setLastMove(game.getMoveHistory().getLastMove());
    }

    public Position getSquarePosition(Point mousePoint) {
        int widthCellSize = getWidth() / SIZE;
        int heightCellSize = getHeight() / SIZE;

        // Get File and Rank
        int min = 0;
        int max = SIZE - 1;
        int file = Math.max(min, Math.min(max, mousePoint.x / widthCellSize));
        int rank = Math.max(min, Math.min(max, max - (mousePoint.y / heightCellSize)));

        // Get Position
        try {
            Position pos = new Position(file, rank);

            // Inverse Position if black's perspective
            if (perspective == Color.WHITE) { return pos; }
            return pos.inverse();
        } catch (IllegalPositionException e) {
            System.err.println("IllegalNotationException: getSquarePosition click not recognized.");
        }
        return null;
    }

    public void setLastMove(Move move) {
        // Remove Old Last Move
        if (lastMove != null) {
            SquarePanel initialSquare = getSquare(lastMove.initialPosition());
            SquarePanel finalSquare = getSquare(lastMove.finalPosition());
            initialSquare.setHighLight(false);
            finalSquare.setHighLight(false);
        }

        // Add New Last Move
        lastMove = move;
        if (move == null) { return; }
        SquarePanel newInitialSquare = getSquare(move.initialPosition());
        SquarePanel newFinalSquare = getSquare(move.finalPosition());
        newInitialSquare.setHighLight(true);
        newFinalSquare.setHighLight(true);
    }

    public void setHighlight(Position position, boolean isHighlighted) {
        SquarePanel square = getSquare(position);
        if (square != null) {
            square.setHighLight(isHighlighted);
        }
    }

    public void setHint(Position position, boolean isHinted) {
        SquarePanel square = getSquare(position);
        if (square != null) {
            square.setHint(isHinted);
        }
    }

    public void clearHints() {
        for (int file = 0; file < SIZE; file++) {
            for (int rank = 0; rank < SIZE; rank++) {
                setHint(new Position(file, rank), false);
            }
        }
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

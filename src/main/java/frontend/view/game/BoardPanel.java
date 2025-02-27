package frontend.view.game;

import engine.game.Board;
import engine.types.Position;
import engine.pieces.Piece;

import frontend.view.utils.PieceImageLoader;
import utils.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BoardPanel extends JPanel{
    private final SquareButton[][] squares = new SquareButton[SIZE][SIZE];
    public static int SIZE = 8;
    public boolean isInitialized = false;

    public BoardPanel() {
        // Setup
        setLayout(new GridLayout(SIZE, SIZE, 0, 0));

        // Background
        setBackground(new java.awt.Color(0xE5E5E5));
    }

    public void initializeBoard(Color perspective) {
        // Cannot Initialize Twice
        if (isInitialized) {
            throw new RuntimeException("Error: BoardPanel.initializeBoard() must be called only once.");
        }

        // Initialize Squares
        PieceImageLoader pieceImageLoader = new PieceImageLoader();
        if (perspective == Color.WHITE) {
            // White
            for (int rank = SIZE - 1; rank >= 0; rank--) {
                for (int file = 0; file < SIZE; file++) {
                    squares[file][rank] = new SquareButton((file + rank) % 2 == 0 ? Color.BLACK : Color.WHITE, pieceImageLoader);
                    add(squares[file][rank]);
                }
            }
        } else if (perspective == Color.BLACK) {
            // Black
            for (int rank = 0; rank < SIZE; rank++) {
                for (int file = SIZE - 1; file >= 0; file--) {
                    squares[file][rank] = new SquareButton((file + rank) % 2 == 0 ? Color.BLACK : Color.WHITE, pieceImageLoader);
                    add(squares[file][rank]);
                }
            }
        }
        isInitialized = true;
    }

    public SquareButton getSquareButton(int file, int rank) {
        if (file < 0 || file >= SIZE || rank < 0 || rank >= SIZE) {
            throw new IllegalArgumentException("Cannot get SquareButton at Invalid Position: " + file + ", " + rank + ". Valid range for file and rank is 0-7.");
        }

        if (!isInitialized) {
            throw new RuntimeException("Error: BoardPanel.initializeBoard() must be called before getting SquareButton.");
        }
        return squares[file][rank];
    }

    public void loadFromBoard(Board board) {
        for (int file = 0; file < SIZE; file++) {
            for (int rank = 0; rank < SIZE; rank++) {
                Piece currentPiece = board.getPieceAt(new Position(file, rank));

                // Set Current Piece Char
                Character currentPieceChar = null;
                if (currentPiece != null) {
                    currentPieceChar = currentPiece.toChar();
                }

                // Get Square Button
                SquareButton squareButton = getSquareButton(file, rank);

                // Set Piece
                squareButton.setPiece(currentPieceChar);

                // Clear Layovers
                squareButton.clearOverlays();
            }
        }
    }

    public void clearPieceOverlays() {
        for (int file = 0; file < SIZE; file++) {
            for (int rank = 0; rank < SIZE; rank++) {
                SquareButton currentSquare = getSquareButton(file, rank);
                currentSquare.clearOverlays();
            }
        }
    }












    public void createJFrame() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess - BoardPanel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(700, 700));
            frame.setLayout(new BorderLayout());
            frame.setResizable(false);

            // Add the BoardPanel to the frame
            frame.add(this, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
    public static void main(String[] args) {
        BoardPanel boardPanel = new BoardPanel();
        boardPanel.createJFrame();
        boardPanel.initializeBoard(Color.WHITE);
    }
}

package frontend.view.game;

import engine.board.Board;
import engine.board.Position;
import engine.pieces.Piece;

import utils.Color;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel{
    private final SquareButton[][] squares = new SquareButton[SIZE][SIZE];
    public static int SIZE = 8;
    public boolean isInitialized = false;

    public BoardPanel() {
        // Setup
        setLayout(new GridLayout(SIZE, SIZE, 0, 0));
        setPreferredSize(new Dimension((SIZE * SquareButton.SIZE), (SIZE * SquareButton.SIZE)));

        // Background
        setBackground(new java.awt.Color(0xE5E5E5));
    }

    public void initializeBoard(Color perspective) {
        // Cannot Initialize Twice
        if (isInitialized) {
            throw new RuntimeException("Error: BoardPanel.initializeBoard() must be called only once.");
        }

        if (perspective == Color.WHITE) {
            // White
            for (int rank = SIZE - 1; rank >= 0; rank--) {
                for (int file = 0; file < SIZE; file++) {
                    squares[file][rank] = new SquareButton((file + rank) % 2 == 0 ? Color.WHITE : Color.BLACK);
                    add(squares[file][rank]);
                }
            }
        } else if (perspective == Color.BLACK) {
            // Black
            for (int rank = 0; rank < SIZE; rank++) {
                for (int file = SIZE - 1; file >= 0; file--) {
                    squares[file][rank] = new SquareButton((file + rank) % 2 == 0 ? Color.WHITE : Color.BLACK);
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

        SquareButton squareButton = squares[file][rank];
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

                // If Square Button Null
                if (squareButton == null) {
                    throw new RuntimeException("Error: BoardPanel.initializeBoard() must be called before loading board.");
                }

                // Set Piece
                squareButton.setPiece(currentPieceChar);
            }
        }
    }

    public void clearPieceOverlays() {
        for (int file = 0; file < SIZE; file++) {
            for (int rank = 0; rank < SIZE; rank++) {
                SquareButton currentSquare = getSquareButton(file, rank);
                currentSquare.setHint(false);
                currentSquare.setActive(false);
            }
        }
    }












    public void createJFrame() {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Chess - BoardPanel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(new Dimension(600, 600));
            frame.setResizable(false);
            frame.setLayout(new GridBagLayout());

            // Grid Bag
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;                        // Column 0
            gbc.gridy = 0;                        // Row 0
            gbc.anchor = GridBagConstraints.WEST; // Anchor to the left (west)
            gbc.weightx = 0;                      // No horizontal expansion
            gbc.weighty = 0;                      // No vertical expansion
            gbc.fill = GridBagConstraints.NONE;   // Don't let it stretch
            gbc.insets = new Insets(0, 0, 0, 0);  // No extra padding

            // Add the BoardPanel to the frame
            frame.add(this, gbc);

            // Add a filler to consume remaining space
            gbc.gridx = 1;                        // Column to the right
            gbc.weightx = 1.0;                    // Consume remaining horizontal space
            gbc.fill = GridBagConstraints.HORIZONTAL; // Filler expands horizontally
            frame.add(Box.createHorizontalGlue(), gbc);

            frame.setVisible(true);
        });
    }
    public static void main(String[] args) {
        BoardPanel boardPanel = new BoardPanel();
        boardPanel.createJFrame();
    }
}

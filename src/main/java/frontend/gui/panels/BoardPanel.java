package frontend.gui.panels;

import engine.board.Board;
import engine.board.Position;
import engine.pieces.Piece;

import frontend.gui.components.SquareButton;
import frontend.Color;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class BoardPanel extends JPanel{
    private final SquareButton[][] squares = new SquareButton[SIZE][SIZE];
    public static int SIZE = 8;
    public static int BORDER_SIZE = 10;

    public BoardPanel(Color perspective) {
        /// Setup
        setLayout(new GridLayout(SIZE, SIZE, 0, 0));
        setPreferredSize(new Dimension((SIZE * SquareButton.SIZE) + 2*BORDER_SIZE, (SIZE * SquareButton.SIZE) + 2*BORDER_SIZE));

        // Background
        setBackground(new java.awt.Color(0xE5E5E5));

        // Border
        Border lineBorder = BorderFactory.createLineBorder(java.awt.Color.WHITE, BORDER_SIZE, true);
        setBorder(lineBorder);

        /// Initialize
        initializeBoard(perspective);
    }

    private void initializeBoard(Color perspective) {
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
    }

    public SquareButton getSquareButton(int file, int rank) {
        return squares[file][rank];
    }

    public void loadFromBoard(Board board) {
        for (int file = 0; file < SIZE; file++) {
            for (int rank = 0; rank < SIZE; rank++) {
                Piece currentPiece = board.getPieceAt(new Position(file, rank));
                Character currentPieceChar = null;

                // If Not Null
                if (currentPiece != null) {
                    currentPieceChar = currentPiece.toChar();
                }

                // Show Piece
                getSquareButton(file, rank).setPiece(currentPieceChar);
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
}

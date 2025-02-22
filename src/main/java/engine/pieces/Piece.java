package engine.pieces;

import engine.board.Board;
import engine.board.Position;

import engine.utils.Move;
import engine.utils.PieceUtils;

import utils.Color;


/**
 * Abstract base class for representing a chess piece.
 * This class provides common behavior and validations for all chess pieces
 * while allowing specific movement logic to be defined by concrete subclasses.
 */
public abstract class Piece{
    private final Color color;

    /**
     * Constructor to initialize the piece's color.
     *
     * @param color The color of the piece (`Color.WHITE` or `Color.BLACK`).
     */
    public Piece(Color color) {
        this.color = color;
    }

    // Getters
    public Color getColor() { return this.color; }
    public Piece getDeepCopy() { return PieceUtils.getDeepCopy(this); }

    // To-ers
    @Override
    public String toString() { return String.valueOf(toChar()); }
    public char toChar() { return PieceUtils.pieceToChar(this); }

    /**
     * Performs generic validations for a move applicable to all pieces.
     * For example, this method checks if the destination square is occupied by a piece of the same color.
     *
     * @param move The move to validate.
     * @param board The current state of the chess board.
     * @return `true` if the move passes generic validations; `false` otherwise.
     */
    protected boolean isMoveGenericValid(Move move, Board board) {
        Position initial = move.getInitialPosition();
        Position finalPos = move.getFinalPosition();

        // Can't capture piece of same color
        Piece targetPiece = board.getPieceAt(finalPos);
        if (targetPiece != null && targetPiece.getColor() == this.getColor()) {
            return false;
        }

        // Add any other shared generic validations.
        return true;
    }

    /**
     * Abstract method for piece-specific move validation.
     * Each specific piece (e.g., King, Queen, Bishop) must implement its own movement rules.
     *
     * @param move The move to validate.
     * @param board The current state of the chess board.
     * @return `true` if the move is valid according to the piece's rules; `false` otherwise.
     */
    public abstract boolean isPieceSpecificMoveValid(Move move, Board board);

    /**
     * Validates a move for the piece by combining generic and piece-specific rules.
     *
     * @param move The move to validate.
     * @param board The current state of the chess board.
     * @return `true` if the move is valid according to all rules; `false` otherwise.
     */
    public boolean isMoveValid(Move move, Board board) {
        return isMoveGenericValid(move, board) && isPieceSpecificMoveValid(move, board);
    }
}

package com.nathanholmberg.chess.engine.pieces;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.Board;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;
import com.nathanholmberg.chess.engine.utils.PieceUtils;

public abstract class Piece{
    private final Color color;

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
     * Executes special moves for the piece, such as "en passant", "castling", or "pawn promotion".
     * This method is intended to apply modifications to the board state that are specific
     * to these unique moves, which involve additional rules and behavior beyond standard piece movement.
     * This method is designed to be overridden in subclasses for pieces that support special moves.
     * For example:
     * - Pawns handle "en passant" captures and promotion to another piece.
     * - Kings handle "castling", which involves moving both the king and a rook.
     * By default, this method does nothing in the base class, as not all pieces have special moves.
     *
     * @param move The move to execute, containing information about its starting and ending positions.
     * @param board The current chess board state, which will be updated to reflect the special move.
     */
    public void specialMoveExecution(Move move, Board board) { board.setEnPassantPosition(null); }

    /**
     * Performs generic validations for a move applicable to all pieces.
     * For example, this method checks if the destination square is occupied by a piece of the same color.
     *
     * @param move The move to validate.
     * @param board The current state of the chess board.
     * @return `true` if the move passes generic validations; `false` otherwise.
     */
    protected boolean passesGenericRules(Move move, Board board) {
        Position finalPosition = move.finalPosition();

        // Can't capture piece of same color
        Piece targetPiece = board.getPieceAt(finalPosition);
        return targetPiece == null || targetPiece.getColor() != this.getColor();
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
        return passesGenericRules(move, board) && isPieceSpecificMoveValid(move, board);
    }
}

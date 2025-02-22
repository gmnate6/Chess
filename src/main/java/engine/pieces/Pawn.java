package engine.pieces;

import engine.game.Board;
import engine.utils.Position;

import engine.utils.Move;

import utils.Color;

/**
 * Represents a Pawn piece in chess.
 * Implements specific pawn movement logic, including special moves like double advance and en passant.
 */
public class Pawn extends Piece {

    /**
     * Constructor to initialize a Pawn with a specific color.
     *
     * @param color The color of the Pawn (`Color.WHITE` or `Color.BLACK`).
     */
    public Pawn(Color color) { super(color); }

    /**
     * Validates whether a given move adheres to the Pawn's movement rules.
     *
     * @param move  The move to validate (initial and final positions).
     * @param board The current board to check for move legality.
     * @return `true` if the move is valid for a Pawn; otherwise, `false`.
     */
    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        return
                isLegalSingle(move, board) ||
                isLegalDouble(move, board) ||
                isLegalCapture(move, board) ||
                isLegalEnPassant(move, board);
    }

    /**
     * Validates a single-step forward move for the Pawn.
     *
     * @param move  The move to validate.
     * @param board The current board to check for obstructions.
     * @return `true` if the move is a valid single-step forward move; otherwise, `false`.
     */
    private boolean isLegalSingle(Move move, Board board) {
        Position initial = move.initialPosition();
        Position finalPos = move.finalPosition();
        int direction = (getColor() == Color.WHITE) ? 1 : -1;

        // Moving straight forward one square
        return finalPos.file() == initial.file() &&
                finalPos.rank() - initial.rank() == direction &&
                board.getPieceAt(finalPos) == null;
    }

    /**
     * Validates a double-step forward move for the Pawn from its starting position.
     *
     * @param move  The move to validate.
     * @param board The current board to check for obstructions.
     * @return `true` if the move is a valid two-square forward move; otherwise, `false`.
     */
    public boolean isLegalDouble(Move move, Board board) {
        Position initial = move.initialPosition();
        Position finalPos = move.finalPosition();
        int direction = (getColor() == Color.WHITE) ? 1 : -1;

        return finalPos.file() == initial.file() &&
                finalPos.rank() - initial.rank() == 2 * direction &&
                board.getPieceAt(finalPos) == null &&
                initial.rank() == ((getColor() == Color.WHITE) ? 1 : 6) &&
                board.getPieceAt(initial.move(0, direction)) == null;
    }

    /**
     * Validates a diagonal capture move for the Pawn.
     *
     * @param move  The move to validate.
     * @param board The current board to check for the target piece.
     * @return `true` if the move is a valid capture; otherwise, `false`.
     */
    private boolean isLegalCapture(Move move, Board board) {
        Position initial = move.initialPosition();
        Position finalPos = move.finalPosition();
        int direction = (getColor() == Color.WHITE) ? 1 : -1;

        Piece target = board.getPieceAt(finalPos);
        return target != null &&
                target.getColor() != this.getColor() &&
                finalPos.rank() - initial.rank() == direction &&
                Math.abs(finalPos.file() - initial.file()) == 1;
    }

    /**
     * Validates an en passant capture for the Pawn.
     *
     * @param move  The move to validate.
     * @param board The current board to check for en passant conditions.
     * @return `true` if the move is a valid en passant capture; otherwise, `false`.
     */
    public boolean isLegalEnPassant(Move move, Board board) {
        Position initial = move.initialPosition();
        Position finalPos = move.finalPosition();

        Position enPassantTarget = board.getEnPassantPosition();
        return finalPos.equals(enPassantTarget) && Math.abs(finalPos.file() - initial.file()) == 1;
    }
}
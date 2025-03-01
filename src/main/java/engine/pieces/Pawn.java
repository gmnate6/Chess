package engine.pieces;

import engine.game.Board;
import engine.types.Position;
import engine.types.Move;
import engine.utils.PieceUtils;
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
     * Executes special moves such as "en passant", "castling", or "pawn promotion".
     * Updates the board to reflect the effects of these moves (e.g., removing a pawn during en passant
     * or moving the rook during castling).
     * Assumes `Board.enPassantPosition` is set to `null` before being called.
     * This method does nothing in the base class and must be overridden by pieces
     * with special move behavior (e.g., `Pawn`, `King`).
     *
     * @param move  The move to execute, containing origin and destination positions.
     * @param board The chess board, updated to reflect the special move.
     */
    @Override
    public void specialMoveExecution(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // If enPassant
        if (isLegalEnPassant(move, board)) {
            // Remove En Passanted Pawn
            Position removedPawnPos = new Position(finalPosition.file(), initialPosition.rank());
            board.setPieceAt(removedPawnPos, null);
        }

        // Update enPassantPosition
        if (isLegalDouble(move, board)) {
            Position newEnPassantPosition = initialPosition.move(0, this.getColor() == Color.WHITE ? 1 : -1);
            board.setEnPassantPosition(newEnPassantPosition);
        } else {
            board.setEnPassantPosition(null);
        }

        // Promotion
        if (isLegalPromotion(move, board)) {
            board.setPieceAt(initialPosition, PieceUtils.charToPiece(move.promotionPiece(), getColor()));
        }
    }

    /**
     * Checks if the given move is a legal promotion for the Pawn.
     * A legal promotion occurs when a Pawn reaches the 8th rank (for White) or the 1st rank (for Black).
     *
     * @param move  The move to validate.
     * @param board The board to check for conditions related to promotion.
     * @return `true` if the move results in a legal promotion, `false` otherwise.
     */
    public boolean isLegalPromotion(Move move, Board board) {
        Position finalPosition = move.finalPosition();

        // Must be Legal Single
        if (!isLegalSingle(move, board) && !isLegalCapture(move, board)) {
            return false;
        }

        return finalPosition.rank() == 0 || finalPosition.rank() == 7;
    }

    /**
     * Validates whether a given move adheres to the Pawn's movement rules.
     *
     * @param move  The move to validate (initial and final positions).
     * @param board The current board to check for move legality.
     * @return `true` if the move is valid for a Pawn; otherwise, `false`.
     */
    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        // En Passant
        if (isLegalEnPassant(move, board)) { return true; }

        // Capture
        if (isLegalCapture(move, board)) { return true; }

        // Double
        if (isLegalDouble(move, board)) { return true; }

        // Single
        return isLegalSingle(move, board);
    }

    /**
     * Validates a single-step forward move for the Pawn.
     *
     * @param move  The move to validate.
     * @param board The current board to check for obstructions.
     * @return `true` if the move is a valid single-step forward move; otherwise, `false`.
     */
    private boolean isLegalSingle(Move move, Board board) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        int fileDirection = this.getColor() == Color.WHITE ? 1 : -1;

        // Must be same file
        if (finalPosition.file() != initialPosition.file()) { return false; }

        // Must be 1 rank away
        if (finalPosition.rank() - initialPosition.rank() != fileDirection) { return false; }

        // No Piece on Final
        return board.getPieceAt(finalPosition) == null;
    }

    /**
     * Validates a double-step forward move for the Pawn from its starting position.
     *
     * @param move  The move to validate.
     * @param board The current board to check for obstructions.
     * @return `true` if the move is a valid two-square forward move; otherwise, `false`.
     */
    public boolean isLegalDouble(Move move, Board board) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        int fileDirection = this.getColor() == Color.WHITE ? 1 : -1;

        // Initial Rank must be 1 (white) or 6 (black)
        if (initialPosition.rank() != (getColor() == Color.WHITE ? 1 : 6)) { return false; }

        // Must be same file
        if (finalPosition.file() != initialPosition.file()) { return false; }

        // Must be 2 rank away
        if (finalPosition.rank() - initialPosition.rank() != 2 * fileDirection) { return false; }

        // No Piece on Final
        if (board.getPieceAt(finalPosition) != null) { return false; }

        // No Piece in front of initial
        return board.getPieceAt(initialPosition.move(0, fileDirection)) == null;
    }

    /**
     * Validates a diagonal capture move for the Pawn.
     *
     * @param move  The move to validate.
     * @param board The current board to check for the target piece.
     * @return `true` if the move is a valid capture; otherwise, `false`.
     */
    private boolean isLegalCapture(Move move, Board board) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        int fileDirection = this.getColor() == Color.WHITE ? 1 : -1;

        // Must be 1 file away
        if (Math.abs(finalPosition.file() - initialPosition.file()) != 1) { return false; }

        // Must be 1 rank away
        if (finalPosition.rank() - initialPosition.rank() != fileDirection) { return false; }

        // Must have piece to capture
        return board.getPieceAt(finalPosition) != null;
    }

    /**
     * Validates an en passant capture for the Pawn.
     *
     * @param move  The move to validate.
     * @param board The current board to check for en passant conditions.
     * @return `true` if the move is a valid en passant capture; otherwise, `false`.
     */
    public boolean isLegalEnPassant(Move move, Board board) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // Files are 1 away
        if (Math.abs(finalPosition.file() - initialPosition.file()) != 1) {
            return false;
        }

        // Ranks are 1 away (sign depends on color)
        if (finalPosition.rank() - initialPosition.rank() != (this.getColor() == Color.WHITE ? 1 : -1)) {
            return false;
        }

        // Final has no Piece
        if (board.getPieceAt(finalPosition) != null) {
            return false;
        }

        // enPassant Position must be enPassantAble
        return finalPosition.equals(board.getEnPassantPosition());
    }
}
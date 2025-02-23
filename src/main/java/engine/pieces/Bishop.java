package engine.pieces;

import engine.game.Board;
import engine.utils.Position;

import engine.utils.Move;
import engine.utils.PieceUtils;

import utils.Color;

/**
 * Represents a Bishop piece in chess.
 * Implements specific movement rules for the Bishop.
 */
public class Bishop extends Piece {

    /**
     * Constructor to initialize a Bishop with a specific color.
     *
     * @param color The color of the Bishop (`Color.WHITE` or `Color.BLACK`).
     */
    public Bishop(Color color) { super(color); }

    /**
     * Validates whether a given move adheres to the Bishop's movement rules.
     *
     * @param move  The move to validate (initial and final positions).
     * @param board The current board to check for obstructions.
     * @return `true` if the move is valid for a Bishop; otherwise, `false`.
     */
    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // Must be a diagonal move
        if (Math.abs(initialPosition.file() - finalPosition.file()) != Math.abs(initialPosition.rank() - finalPosition.rank())) {
            return false;
        }

        // Check if the path is clear
        if (!PieceUtils.isPathClear(move, board)) { return false; }

        // Is Legal Move
        return true;
    }
}

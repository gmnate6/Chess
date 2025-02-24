package engine.pieces;

import engine.game.Board;
import engine.types.Position;

import engine.types.Move;
import engine.utils.PieceUtils;

import utils.Color;

/**
 * Represents a Rook piece in chess.
 * Implements the specific movement logic for a Rook.
 */
public class Rook extends Piece {
    /**
     * Constructor to initialize a Rook with a specific color.
     *
     * @param color The color of the Rook (`Color.WHITE` or `Color.BLACK`).
     */
    public Rook(Color color) { super(color); }

    /**
     * Validates whether a given move adheres to the Rook's movement rules.
     *
     * @param move  The move to validate (initial and final positions).
     * @param board The current board to check for obstructions.
     * @return `true` if the move is valid for a Rook; otherwise, `false`.
     */
    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // Must be in the same rank or file
        if (initialPosition.file() != finalPosition.file() && initialPosition.rank() != finalPosition.rank()) { return false; }

        // Check if the path is clear
        return PieceUtils.isPathClear(move, board);
    }
}

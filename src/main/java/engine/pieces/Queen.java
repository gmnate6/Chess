package engine.pieces;

import engine.game.Board;
import engine.utils.Position;

import engine.utils.Move;
import engine.utils.PieceUtils;

import utils.Color;

/**
 * Represents a Queen piece in chess.
 * Implements the specific movement logic for a Queen.
 */
public class Queen extends Piece {

    /**
     * Constructor to initialize a Queen with a specific color.
     *
     * @param color The color of the Queen (`Color.WHITE` or `Color.BLACK`).
     */
    public Queen(Color color) { super(color); }

    /**
     * Validates whether a given move adheres to the Queen's movement rules.
     *
     * @param move  The move to validate (initial and final positions).
     * @param board The current board to check for obstructions.
     * @return `true` if the move is valid for a Queen; otherwise, `false`.
     */
    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // Conditions for queen's movement
        boolean isStraightMove = initialPosition.file() == finalPosition.file() ||
                initialPosition.rank() == finalPosition.rank();
        boolean isDiagonalMove = Math.abs(initialPosition.file() - finalPosition.file()) ==
                Math.abs(initialPosition.rank() - finalPosition.rank());

        // Must be a straight or diagonal move
        if (!(isStraightMove || isDiagonalMove)) { return false; }

        // Check if the path is clear
        return PieceUtils.isPathClear(move, board);
    }
}

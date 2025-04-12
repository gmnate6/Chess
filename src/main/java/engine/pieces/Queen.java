package engine.pieces;

import engine.game.Board;
import engine.types.Position;
import engine.types.Move;
import engine.utils.PieceUtils;
import utils.Color;

public class Queen extends Piece {
    public Queen(Color color) { super(color); }

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

package engine.pieces;

import engine.game.Board;
import engine.types.Position;
import engine.types.Move;
import engine.utils.PieceUtils;
import utils.Color;

public class Bishop extends Piece {
    public Bishop(Color color) { super(color); }

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
        return PieceUtils.isPathClear(move, board);
    }
}

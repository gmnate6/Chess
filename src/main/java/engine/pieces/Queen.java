package engine.pieces;

import engine.board.Board;
import engine.board.Position;
import engine.utils.Move;
import engine.utils.Color;

public class Queen extends Piece {
    public Queen(Color color) { super(color); }

    // Move Legality
    public boolean moveLegality(Move move, Board board) {
        // Super
        if (!super.moveLegality(move, board)) { return false; }

        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();

        // Conditions for queen's movement
        boolean isStraightMove = initialPosition.file() == finalPosition.file() ||
                initialPosition.rank() == finalPosition.rank();
        boolean isDiagonalMove = Math.abs(initialPosition.file() - finalPosition.file()) ==
                Math.abs(initialPosition.rank() - finalPosition.rank());

        // Must be a straight or diagonal move
        if (!(isStraightMove || isDiagonalMove)) { return false; }

        // Check if the path is clear
        if (!this.isPathClear(move, board)) { return false; }

        // Is Legal Move
        return true;
    }
}

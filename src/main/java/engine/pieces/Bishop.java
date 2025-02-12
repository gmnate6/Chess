package engine.pieces;

import engine.board.Board;
import engine.board.Position;
import engine.utils.Move;
import engine.utils.Color;

public class Bishop extends Piece {
    public Bishop(Color color) { super(color); }

    // Move Legality
    public boolean moveLegality(Move move, Board board) {
        // Super
        if (!super.moveLegality(move, board)) { return false; }

        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();

        // Must be a diagonal move
        if (Math.abs(initialPosition.file() - finalPosition.file()) != Math.abs(initialPosition.rank() - finalPosition.rank())) {
            return false;
        }

        // Check if the path is clear
        if (!isPathClear(move, board)) { return false; }

        // Is Legal Move
        return true;
    }
}

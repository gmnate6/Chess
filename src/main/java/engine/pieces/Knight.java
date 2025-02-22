package engine.pieces;

import engine.board.Board;
import engine.board.Position;
import engine.utils.Move;

import utils.Color;

public class Knight extends Piece {
    public Knight(Color color) { super(color); }

    // Move Legality
    public boolean moveLegality(Move move, Board board) {
        // Super
        if (!super.moveLegality(move, board)) { return false; }

        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();

        // Distances
        int deltaFile = Math.abs(finalPosition.file() - initialPosition.file());
        int deltaRank = Math.abs(finalPosition.rank() - initialPosition.rank());

        // 2 in one 1 in another
        if ((deltaFile == 2 && deltaRank == 1) || (deltaFile == 1 && deltaRank == 2)) {
            return true;
        }

        // Not Legal
        return false;
    }
}

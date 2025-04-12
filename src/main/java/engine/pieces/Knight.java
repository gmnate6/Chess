package engine.pieces;

import engine.game.Board;
import engine.types.Position;
import engine.types.Move;
import utils.Color;

public class Knight extends Piece {
    public Knight(Color color) { super(color); }

    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // Distances
        int deltaFile = Math.abs(finalPosition.file() - initialPosition.file());
        int deltaRank = Math.abs(finalPosition.rank() - initialPosition.rank());

        // 2 in one 1 in another
        return (deltaFile == 2 && deltaRank == 1) || (deltaFile == 1 && deltaRank == 2);
    }
}

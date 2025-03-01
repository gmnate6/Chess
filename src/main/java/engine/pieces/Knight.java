package engine.pieces;

import engine.game.Board;
import engine.types.Position;
import engine.types.Move;
import utils.Color;

/**
 * Represents a Knight piece in chess.
 * Implements movement logic specifically for the Knight.
 */
public class Knight extends Piece {
    /**
     * Constructor to initialize a Knight with a specific color.
     *
     * @param color The color of the Knight (`Color.WHITE` or `Color.BLACK`).
     */
    public Knight(Color color) { super(color); }

    /**
     * Validates whether a given move adheres to the Knight's unique movement rules.
     *
     * @param move  The move to validate (initial and final positions).
     * @param board The current board (not used here for path checking).
     * @return `true` if the move is valid for a Knight; otherwise, `false`.
     */
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

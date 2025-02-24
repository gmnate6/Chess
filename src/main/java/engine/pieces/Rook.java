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
     * Executes special moves for the Rook, specifically updates to castling rights.
     * If the Rook is moved from its starting position (e.g., a1, h1 for White or a8, h8 for Black),
     * the corresponding castling rights are revoked.
     * Assumes `Board.enPassantPosition` is set to `null` before this method is called.
     *
     * @param move  The move being executed, containing the Rook's initial position.
     * @param board The current chess board, updated to reflect changes in castling rights.
     */
    @Override
    public void specialMoveExecution(Move move, Board board) {
        super.specialMoveExecution(move, board);

        // Collapse Move Obj
        Position initialPosition = move.initialPosition();

        // Update CastlingRights
        if (getColor() == Color.WHITE) {
            // White
            if (initialPosition.toAlgebraic().equals("a1")) {
                board.getCastlingRights().setWhiteQueenSide(false);
            }
            if (initialPosition.toAlgebraic().equals("h1")) {
                board.getCastlingRights().setWhiteKingSide(false);
            }
        } else {
            // Black
            if (initialPosition.toAlgebraic().equals("a8")) {
                board.getCastlingRights().setBlackQueenSide(false);
            }
            if (initialPosition.toAlgebraic().equals("h8")) {
                board.getCastlingRights().setBlackKingSide(false);
            }
        }
    }

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

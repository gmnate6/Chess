package engine.pieces;

import engine.game.Board;
import engine.utils.Position;

import engine.utils.CastlingRights;
import engine.utils.Move;

import utils.Color;

/**
 * Represents the King piece in chess, including movement and castling logic.
 */
public class King extends Piece {

    /**
     * Constructor to initialize a King piece with the specified color.
     *
     * @param color The color of the King (`Color.WHITE` or `Color.BLACK`).
     */
    public King(Color color) { super(color); }

    /**
     * Validates if a move is valid for the King, specific to its movement rules.
     *
     * @param move  The move to validate.
     * @param board The current state of the chess board.
     * @return `true` if the move is valid for a King; otherwise, `false`.
     */
    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        Position initial = move.initialPosition();
        Position finalPos = move.finalPosition();

        int deltaFile = Math.abs(finalPos.file() - initial.file());
        int deltaRank = Math.abs(finalPos.rank() - initial.rank());

        // Basic king movement: one step in any direction
        if (deltaFile <= 1 && deltaRank <= 1) {
            return true;
        }

        // Castling logic
        return isLegalCastle(move, board);
    }

    /**
     * Determines if a move is a valid castling move for the King.
     *
     * @param move  The move to validate.
     * @param board The current state of the chess board.
     * @return `true` if the castling move is valid; otherwise, `false`.
     */
    public boolean isLegalCastle(Move move, Board board) {
        Position initial = move.initialPosition();
        Position finalPos = move.finalPosition();

        // Determine if move is attempting a castle
        int deltaFile = finalPos.file() - initial.file();
        boolean kingSide = deltaFile > 0;
        if (!isCastleAttempt(move, kingSide)) {
            return false;
        }

        // Validate castling rights, rook presence, and obstruction
        CastlingRights rights = board.getCastlingRights();
        return rights.isCastlingAllowed(getColor(), kingSide) && isCastlingPathClear(initial, kingSide, board) && !isInCheckDuringCastling(initial, kingSide, board);
    }

    /**
     * Checks if the move is a proper castling attempt.
     *
     * @param move     The move to validate.
     * @param kingSide `true` for king-side castling; `false` for queen-side castling.
     * @return `true` if the move appears to be a castling attempt; otherwise, `false`.
     */
    public boolean isCastleAttempt(Move move, boolean kingSide) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // Must be same rank
        if (initialPosition.rank() != finalPosition.rank()) {
            return false;
        }

        // Check delta file
        int deltaFile = finalPosition.file() - initialPosition.file();
        if (kingSide) {
            return deltaFile == 2;
        } else {
            return deltaFile == -2;
        }
    }

    /**
     * Checks if the path for castling is free of obstructions.
     *
     * @param initial  The initial position of the King.
     * @param kingSide `true` for king-side castling; `false` for queen-side.
     * @param board    The current state of the board.
     * @return `true` if the path is clear; otherwise, `false`.
     */
    private boolean isCastlingPathClear(Position initial, boolean kingSide, Board board) {
        int direction = kingSide ? 1 : -1;
        int range = kingSide ? 2 : 3; // Two or three squares must be clear
        for (int i = 1; i <= range; i++) {
            Position intermediate = initial.move(direction * i, 0);
            if (board.getPieceAt(intermediate) != null) {
                return false; // Path obstructed
            }
        }
        return true;
    }

    /**
     * Checks if the King would be in check during any point in castling.
     *
     * @param initial  The initial position of the King.
     * @param kingSide `true` for king-side castling; `false` for queen-side.
     * @param board    The current state of the board.
     * @return `true` if the King would be in check during castling; otherwise, `false`.
     */
    private boolean isInCheckDuringCastling(Position initial, boolean kingSide, Board board) {
        int direction = kingSide ? 1 : -1;
        for (int i = 0; i <= 2; i++) { // Include intermediate square and final square
            Position intermediate = initial.move(direction * i, 0);
            if (isChecked(intermediate, board)) {
                return true; // King would be in check
            }
        }
        return false;
    }

    /**
     * Checks if the King is in check at a given position.
     * Considers all possible attackers on the board.
     *
     * @param position The position to check.
     * @param board    The current state of the chess board.
     * @return `true` if the King is in check; otherwise, `false`.
     */
    public boolean isChecked(Position position, Board board) {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position attackerPos = new Position(file, rank);
                Piece attacker = board.getPieceAt(attackerPos);

                if (attacker != null &&
                        attacker.getColor() != this.getColor() &&
                        attacker.isMoveValid(new Move(attackerPos, position, '\0'), board)) {
                    return true; // Enemy piece attacks this square
                }
            }
        }
        return false;
    }
}

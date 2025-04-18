package com.nathanholmberg.chess.engine.pieces;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.Board;
import com.nathanholmberg.chess.engine.types.CastlingRights;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;

public class King extends Piece {
    public King(Color color) { super(color); }

    @Override
    public void specialMoveExecution(Move move, Board board) {
        super.specialMoveExecution(move, board);

        // Collapse Move Obj
        Position initialPosition = move.initialPosition();

        // If Castle King Side
        if (isCastleAttempt(move, true)) {
            Position rookPosition = initialPosition.move(3, 0);
            Piece rookPiece = board.getPieceAt(rookPosition);

            // Place Rook
            board.setPieceAt(initialPosition.move(1, 0), rookPiece);

            // Remove Old Rook
            board.setPieceAt(rookPosition, null);
        }

        // If Castle Queen Side
        if (isCastleAttempt(move, false)) {
            Position rookPosition = initialPosition.move(-4, 0);
            Piece rookPiece = board.getPieceAt(rookPosition);

            // Place Rook
            board.setPieceAt(initialPosition.move(-1, 0), rookPiece);

            // Remove Old Rook
            board.setPieceAt(rookPosition, null);
        }

        // Update CastlingRights
        if (getColor() == Color.WHITE) {
            board.getCastlingRights().setWhiteKingSide(false);
            board.getCastlingRights().setWhiteQueenSide(false);
        } else {
            board.getCastlingRights().setBlackKingSide(false);
            board.getCastlingRights().setBlackQueenSide(false);
        }
    }

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

    public boolean isCastleAttempt(Move move) {
        return isCastleAttempt(move, true) || isCastleAttempt(move, false);
    }

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

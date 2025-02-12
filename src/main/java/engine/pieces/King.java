package engine.pieces;

import engine.board.Board;
import engine.board.Position;
import engine.utils.CastlingRights;
import engine.utils.Move;
import engine.utils.Color;

public class King extends Piece {
    public King(Color color) { super(color); }

    // Move Legality
    public boolean moveLegality(Move move, Board board) {
        // Super
        if (!super.moveLegality(move, board)) { return false; }

        // Is Legal Castle
        if (isLegalCastle(move, board, 'k')) { return true; }
        if (isLegalCastle(move, board, 'q')) { return true; }

        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();

        // Distances
        int deltaFile = Math.abs(finalPosition.file() - initialPosition.file());
        int deltaRank = Math.abs(finalPosition.rank() - initialPosition.rank());

        // King can only move 1 space
        if (deltaFile <= 1 && deltaRank <= 1) {
            return true;
        }

        return false;
    }

    // True if in check
    public boolean isChecked(Position myPosition, Board board) {
        // Loop Through Board
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position currentPosition = new Position(file, rank);
                Piece currentPiece = board.getPieceAt(currentPosition);
                Move currentMove = new Move(currentPosition, myPosition, '\0');

                // Skip Null
                if (currentPiece == null) {
                    continue;
                }

                // Skip Same Color
                if (currentPiece.getColor() == this.getColor()) {
                    continue;
                }

                // Check if Piece can capture
                if (currentPiece.moveLegality(currentMove, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    // True if given move is a legal castle
    public boolean isLegalCastle(Move move, Board board, char side) {
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();
        CastlingRights castlingRightsObj = board.getCastlingRights();

        // Determine if it's king-side or queen-side
        if (Character.toUpperCase(side) != 'K' && Character.toUpperCase(side) != 'Q') {
            throw new IllegalArgumentException("King.canCastle()'s side must be 'K' or 'Q'");
        }
        boolean isKingSide = Character.toUpperCase(side) == 'K';

        // Check Castling Rights
        boolean castlingRights = (this.getColor() == Color.WHITE)
                ? (isKingSide ? castlingRightsObj.isWhiteKingSide() : castlingRightsObj.isWhiteQueenSide())
                : (isKingSide ? castlingRightsObj.isBlackKingSide() : castlingRightsObj.isBlackQueenSide());
        if (!castlingRights) {
            return false;
        }

        // Determine positions based on side
        int direction = isKingSide ? 1 : -1;
        int range = isKingSide ? 2 : 3; // 2 for king-side, 3 for queen-side

        // Check for Correct Final Position
        if (finalPosition.file() - initialPosition.file() != 2 * direction || finalPosition.rank() != initialPosition.rank()) {
            return false;
        }

        // Check for Obstructions
        for (int i = 1; i <= range; i++) {
            Position p = initialPosition.move(direction * i, 0);
            if (board.getPieceAt(p) != null) {
                return false;
            }
        }

        // Check for Rook
        Position rookPosition = initialPosition.move(isKingSide ? 3 : -4, 0);
        if (!(board.getPieceAt(rookPosition) instanceof Rook)) {
            return false;
        }

        // Check for Check Conditions
        // for (int i = 0; i <= range; i++) {
        //     Position p = initialPosition.move(direction * i, 0);
        //     if (this.isChecked(p, board)) {
        //         return false;
        //     }
        // }

        // Return
        return true;
    }
}

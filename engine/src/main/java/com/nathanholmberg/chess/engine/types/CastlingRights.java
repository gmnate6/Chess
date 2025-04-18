package com.nathanholmberg.chess.engine.types;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.Board;
import com.nathanholmberg.chess.engine.pieces.Rook;

public class CastlingRights {
    private boolean whiteKingSide;
    private boolean whiteQueenSide;
    private boolean blackKingSide;
    private boolean blackQueenSide;

    public CastlingRights() {
        this.whiteKingSide = true;
        this.whiteQueenSide = true;
        this.blackKingSide = true;
        this.blackQueenSide = true;
    }

    public CastlingRights getDeepCopy() {
        CastlingRights copy = new CastlingRights();
        copy.setWhiteKingSide(whiteKingSide);
        copy.setWhiteQueenSide(whiteQueenSide);
        copy.setBlackKingSide(blackKingSide);
        copy.setBlackQueenSide(blackQueenSide);
        return copy;
    }

    // Getters
    public boolean isWhiteKingSide() { return whiteKingSide; }
    public boolean isWhiteQueenSide() { return whiteQueenSide; }
    public boolean isBlackKingSide() { return blackKingSide; }
    public boolean isBlackQueenSide() { return blackQueenSide; }
    public boolean isNone() {
        if (isWhiteKingSide()) return false;
        if (isWhiteQueenSide()) return false;
        if (isBlackKingSide()) return false;
        return !isBlackQueenSide();
    }

    public boolean isCastlingAllowed(Color color, boolean kingSide) {
        if (color == Color.WHITE) {
            if (kingSide) {
                return isWhiteKingSide();
            } else {
                return isWhiteQueenSide();
            }
        }
        if (color == Color.BLACK) {
            if (kingSide) {
                return isBlackKingSide();
            } else {
                return isBlackQueenSide();
            }
        }
        return false;
    }

    // Setters
    public void setWhiteKingSide(boolean whiteKingSide) { this.whiteKingSide = whiteKingSide; }
    public void setWhiteQueenSide(boolean whiteQueenSide) { this.whiteQueenSide = whiteQueenSide; }
    public void setBlackKingSide(boolean blackKingSide) { this.blackKingSide = blackKingSide; }
    public void setBlackQueenSide(boolean blackQueenSide) { this.blackQueenSide = blackQueenSide; }

    /**
     * Verifies and adjusts castling rights based on the current board state.
     * Removes castling rights if the associated king or rook is not in its original position.
     *
     * @param board The chess board to verify against.
     */
    public void verifyRights(Board board) {
        // Check for White King
        if (!board.getKingPosition(Color.WHITE).equals("e1")) {
            setWhiteKingSide(false);
            setWhiteQueenSide(false);
        }
        // Check for Black King
        if (!board.getKingPosition(Color.BLACK).equals("e8")) {
            setBlackKingSide(false);
            setBlackQueenSide(false);
        }

        // Check for White Queen's Rook
        if (!(board.getPieceAt(Position.fromAlgebraic("a1")) instanceof Rook)) {
            setWhiteQueenSide(false);
        }
        // Check for White King's Rook
        if (!(board.getPieceAt(Position.fromAlgebraic("h1")) instanceof Rook)) {
            setWhiteKingSide(false);
        }

        // Check for Black Queen's Rook
        if (!(board.getPieceAt(Position.fromAlgebraic("a8")) instanceof Rook)) {
            setBlackQueenSide(false);
        }
        // Check for Black King's Rook
        if (!(board.getPieceAt(Position.fromAlgebraic("h8")) instanceof Rook)) {
            setBlackKingSide(false);
        }
    }
}

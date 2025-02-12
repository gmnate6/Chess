package engine.utils;

import engine.board.Board;
import engine.board.Position;

import engine.pieces.*;

public final class Move {
    private final Position initialPosition;
    private final Position finalPosition;
    private final char promotionPiece;

    // Constructor
    public Move(Position initalPosition, Position finalPosition, char promotionPiece) {
        this.initialPosition = initalPosition;
        this.finalPosition = finalPosition;
        if (Character.toUpperCase(promotionPiece) == 'K') {
            throw new IllegalArgumentException("Illegal Move: Cannot promote to King.");
        }
        this.promotionPiece = Character.toUpperCase(promotionPiece);
    }

    // Is Move Safe
    public boolean isMoveSafe(Board board , Color currentPlayer) {
        // Create a copy of the board
        Board boardCopy = board.getDeepCopy();
        // Apply Move
        boardCopy.move(this);
        // Check if the king is in check after the move
        return !boardCopy.isKingInCheck(currentPlayer);
    }

    // Getters
    public Position getInitialPosition() { return this.initialPosition; }
    public Position getFinalPosition() { return this.finalPosition; }
    public char getPromotionPiece() { return this.promotionPiece; }

    // To String
    public String toString() {
        return initialPosition.toString() + " -> " + finalPosition.toString();
    }

    // Equality & Hashing
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Move other)) return false;
        return this.initialPosition == other.initialPosition && this.finalPosition == other.finalPosition;
    }
    @Override
    public int hashCode() {
        return 31 * initialPosition.hashCode() + finalPosition.hashCode();
    }
}

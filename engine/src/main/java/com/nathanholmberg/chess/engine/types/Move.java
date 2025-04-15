package com.nathanholmberg.chess.engine.types;

import com.nathanholmberg.chess.engine.exceptions.IllegalMoveException;

public record Move(Position initialPosition, Position finalPosition, char promotionPiece) {
    public Move(Position initialPosition, Position finalPosition, char promotionPiece) {
        this.initialPosition = initialPosition;
        this.finalPosition = finalPosition;
        this.promotionPiece = Character.toUpperCase(promotionPiece);
        if ("QRBN".indexOf(this.promotionPiece) == -1 && this.promotionPiece != '\0') {
            throw new IllegalMoveException("Illegal Move: Promotion Piece must be a Queen, Rook, Bishop, or Knight.");
        }
    }

    public String toString() {
        return initialPosition.toString() + " -> " + finalPosition.toString() + " (" + promotionPiece + ")";
    }

    // Equality & Hashing
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Move other)) return false;
        return this.initialPosition.equals(other.initialPosition) && this.finalPosition.equals(other.finalPosition) && this.promotionPiece == other.promotionPiece;
    }
    @Override
    public int hashCode() {
        return 31 * initialPosition.hashCode() + finalPosition.hashCode();
    }
}

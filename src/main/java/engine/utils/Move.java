package engine.utils;

import engine.board.Position;

/**
 * The `Move` class represents a single move in a board-based game, encapsulating
 * the starting position, destination position, and an optional promotion piece.
 * It is immutable and enforces specific constraints, such as disallowing promotion to a king.
 */
public final class Move {
    private final Position initialPosition;
    private final Position finalPosition;
    private final char promotionPiece;

    /**
     * Constructs a new `Move` instance.
     *
     * @param initalPosition The starting position of the move.
     * @param finalPosition The destination position of the move.
     * @param promotionPiece The piece for promotion (if applicable). Pass '\u0000' if no promotion is needed.
     *                       The promotion piece is stored in uppercase.
     *                       Promotion to a king ('K') is prohibited.
     * @throws IllegalArgumentException If the promotion piece is a king ('K').
     */
    public Move(Position initalPosition, Position finalPosition, char promotionPiece) {
        this.initialPosition = initalPosition;
        this.finalPosition = finalPosition;
        if (Character.toUpperCase(promotionPiece) == 'K') {
            throw new IllegalArgumentException("Illegal Move: Cannot promote to King.");
        }
        this.promotionPiece = Character.toUpperCase(promotionPiece);
    }

    // Getters
    public Position getInitialPosition() { return this.initialPosition; }
    public Position getFinalPosition() { return this.finalPosition; }
    public char getPromotionPiece() { return this.promotionPiece; }

    /**
     * Converts the move to its string representation.
     *
     * @return The string representation in the format "[initialPosition] -> [finalPosition]".
     *         For example: "A2 -> A4".
     */
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

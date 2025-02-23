package engine.types;

/**
 * The `Move` class represents a single move in a board-based game, encapsulating
 * the starting position, destination position, and an optional promotion piece.
 * It is immutable and enforces specific constraints, such as disallowing promotion to a king.
 */
public record Move(Position initialPosition, Position finalPosition, char promotionPiece) {
    /**
     * Constructs a new `Move` instance.
     *
     * @param initialPosition The starting position of the move.
     * @param finalPosition   The destination position of the move.
     * @param promotionPiece  The piece for promotion (if applicable). Pass '\u0000' if no promotion is needed.
     *                        The promotion piece is stored in uppercase.
     *                        Promotion to a king ('K') is prohibited.
     * @throws IllegalArgumentException If the promotion piece is a king ('K').
     */
    public Move(Position initialPosition, Position finalPosition, char promotionPiece) {
        this.initialPosition = initialPosition;
        this.finalPosition = finalPosition;
        if (Character.toUpperCase(promotionPiece) == 'K') {
            throw new IllegalArgumentException("Illegal Move: Cannot promote to King.");
        }
        this.promotionPiece = Character.toUpperCase(promotionPiece);
    }

    /**
     * Creates a new `Move` object by parsing a move in Long Algebraic Notation.
     * Long Algebraic Notation is a string format that represents a chess move.
     * It consists of a four or five-character string:
     * - The first two characters represent the starting position (e.g., "e2").
     * - The next two characters represent the destination position (e.g., "e4").
     * - An optional fifth character represents the piece used for promotion (e.g., "q" for a queen).
     * For example:
     * - "e2e4" represents a move from e2 to e4 without promotion.
     * - "e7e8q" represents a move from e7 to e8 with promotion to a queen.
     *
     * @param notation The move in Long Algebraic Notation.
     * @return A `Move` object representing the parsed move.
     * @throws IllegalArgumentException If the notation is invalid (e.g., incorrect length or invalid positions).
     */
    public static Move fromLongAlgebraic(String notation) {
        if (notation.length() < 4 || notation.length() > 5) {
            throw new IllegalArgumentException("Invalid Long Algebraic Notation: " + notation);
        }

        // Get Sub Strings
        String from = notation.substring(0, 2);
        String to = notation.substring(2, 4);
        char promotion = notation.length() == 5 ? notation.charAt(4) : '\0';

        // Try to make Positions
        try {
            Position initialPosition = Position.fromAlgebraic(from);
            Position finalPosition = Position.fromAlgebraic(to);

            // Make and Return Move
            return new Move(initialPosition, finalPosition, promotion);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid Long Algebraic Notation: " + notation);
        }
    }

    /**
     * Converts this `Move` object to Long Algebraic Notation.
     * Produces a string that represents the move in Long Algebraic Notation:
     * - The initial position as a two-character code (e.g., "e2").
     * - The final position as a two-character code (e.g., "e4").
     * - If applicable, the promotion piece as the last character (e.g., "q").
     * For example:
     * - A move from e2 to e4 without promotion becomes "e2e4".
     * - A move from e7 to e8 with promotion to a queen becomes "e7e8q".
     *
     * @return A string representation of the move in Long Algebraic Notation format.
     */
    public String toLongAlgebraic() {
        return initialPosition.toAlgebraic() + finalPosition.toAlgebraic() + (promotionPiece == '\0' ? "" : promotionPiece);
    }

    /**
     * Converts the move to its string representation.
     *
     * @return The string representation in the format "[initialPosition] -> [finalPosition]".
     * For example: "A2 -> A4".
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

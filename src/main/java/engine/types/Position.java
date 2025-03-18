package engine.types;

import engine.exceptions.IllegalNotationException;
import engine.exceptions.IllegalPositionException;

/**
 * Represents a position on a chessboard using file (column) and rank (row) coordinates.
 * This class uses a `record` to define an immutable object with `file` and `rank`.
 * Valid file and rank values must fall within the range 0-7 (inclusive).
 */
public record Position(int file, int rank) {
    /**
     * Constructor for `Position`.
     * Ensures the `file` and `rank` values are within the valid range (0-7).
     *
     * @param file The file (column) of the position (0 = 'a', 1 = 'b', ..., 7 = 'h').
     * @param rank The rank (row) of the position (0 = 1, 1 = 2, ..., 7 = 8).
     * @throws IllegalPositionException If the file or rank is out of range.
     */
    public Position {
        if (file < 0 || file > 7 || rank < 0 || rank > 7) {
            throw new IllegalPositionException("Illegal Position: (" + file + ", " + rank + "). Valid range for file and rank is 0-7.");
        }
    }

    /**
     * Creates a `Position` object from algebraic notation (e.g., "e4", "a1").
     *
     * @param notation The algebraic notation of the position (e.g., "a1", "e4").
     * @return The corresponding `Position` object.
     * @throws IllegalNotationException If the notation is invalid.
     */
    public static Position fromAlgebraic(String notation) {
        if (notation.length() != 2) {
            throw new IllegalNotationException("Illegal Position Notation: '" + notation + "'. Notation must be exactly 2 characters.");
        }

        // Parse
        char fileChar = notation.charAt(0);
        char rankChar = notation.charAt(1);

        // Rank must be Digit
        if (!Character.isDigit(rankChar)) {
            throw new IllegalNotationException("Illegal Position Notation: '" + notation + "'. Rank must be a digit (1-8)");
        }

        // Convert to INT
        int file = fileChar - 'a';
        int rank = Character.getNumericValue(rankChar) - 1;

        // Must be in range
        if (file < 0 || file > 7 || rank < 0 || rank > 7) {
            throw new IllegalNotationException("Illegal Position Notation: '" + notation + "'. File and rank must be between 0 and 7.");
        }

        // Return
        return new Position(file, rank);
    }

    /**
     * Converts the position to standard algebraic notation (e.g., "a1", "e4").
     *
     * @return The position in algebraic notation.
     */
    public String toAlgebraic() {
        return "" + fileToChar() + rankToChar();
    }

    /**
     * @return The position's algebraic notation as its string representation.
     */
    @Override
    public String toString() {
        return toAlgebraic();
    }

    /**
     * @return The position's file as a char.
     */
    public char fileToChar() {
        return (char) ('a' + file);
    }

    /**
     * @return The position's rank as a char.
     */
    public char rankToChar() {
        return (char) ('1' + rank);
    }

    /**
     * Moves the position by a specified delta for file and rank.
     *
     * @param fileDelta The change in the file (column).
     * @param rankDelta The change in the rank (row).
     * @return A new `Position` object after the move.
     * @throws IllegalPositionException If the move results in an out-of-bounds position.
     */
    public Position move(int fileDelta, int rankDelta) {
        int newFile = file + fileDelta;
        int newRank = rank + rankDelta;
        if (newFile < 0 || newFile > 7 || newRank < 0 || newRank > 7) {
            throw new IllegalPositionException("Illegal Position: Moved Position is Out of Bounds.");
        }
        return new Position(newFile, newRank);
    }


    /**
     * Flips the position as if the chessboard is turned 180 degrees.
     * The file (column) and rank (row) are both inverted relative to
     * the center of the chessboard.
     *
     * For example:
     * - Position "a1" (file = 0, rank = 0) is inverted to "h8" (file = 7, rank = 7).
     * - Position "h8" (file = 7, rank = 7) is inverted to "a1" (file = 0, rank = 0).
     * - Position "c3" (file = 2, rank = 2) is inverted to "f6" (file = 5, rank = 5).
     *
     * @return A new `Position` object representing the flipped coordinates.
     */
    public Position inverse() {
        return new Position(7 - file, 7 - rank);
    }

    // Equality & Hashing
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Position other)) return false;
        return this.file == other.file && this.rank == other.rank;
    }
    public boolean equals(String notation) {
        Position pos = Position.fromAlgebraic(notation);
        return this.equals(pos);
    }
}

package engine.board;

public record Position(int file, int rank) {
    // Constructor
    public Position {
        if (file < 0 || file > 7 || rank < 0 || rank > 7) {
            throw new IllegalArgumentException("Invalid Position: " + file + ", " + rank + ". Valid range for file and rank is 0-7.");
        }
    }

    // Get Position from Algebraic Notation
    public static Position fromAlgebraic(String notation) {
        if (notation.length() != 2) {
            throw new IllegalArgumentException("Invalid Position: Notation must be exactly 2 characters. Received: " + notation);
        }

        // Parse
        char fileChar = notation.charAt(0);
        char rankChar = notation.charAt(1);

        // Rank must be Digit
        if (!Character.isDigit(rankChar)) {
            throw new IllegalArgumentException("Invalid Position: Rank must be a digit (1-8). Received: " + notation);
        }

        // Convert to INT
        int file = fileChar - 'a';
        int rank = Character.getNumericValue(rankChar) - 1;

        // Must be in range
        if (file < 0 || file > 7 || rank < 0 || rank > 7) {
            throw new IllegalArgumentException("Invalid Position: " + notation + ". File and rank must be between 0 and 7.");
        }

        // Return
        return new Position(file, rank);
    }

    // Convert to algebraic notation
    public String toAlgebraic() {
        return "" + (char) ('a' + file) + (rank + 1);
    }

    @Override
    public String toString() {
        return toAlgebraic();
    }

    // Move a position by (deltaFile, deltaRank)
    public Position move(int fileDelta, int rankDelta) {
        int newFile = file + fileDelta;
        int newRank = rank + rankDelta;
        if (newFile < 0 || newFile > 7 || newRank < 0 || newRank > 7) {
            throw new IllegalArgumentException("Moved Position is Out of Bounds.");
        }
        return new Position(newFile, newRank);
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

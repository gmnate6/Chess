package utils;

public enum GameResult {
    WHITE_CHECKMATE,               // White wins by checkmate
    BLACK_CHECKMATE,               // Black wins by checkmate
    STALEMATE,                     // Draw by stalemate
    FIFTY_MOVE_RULE,               // Draw by 50-move rule
    THREEFOLD_REPETITION,          // Draw by move repetition
    TIME_WHITE_LOSS,               // White loses on time
    TIME_BLACK_LOSS,               // Black loses on time
    DRAW_AGREEMENT,                // Draw by mutual agreement
    RESIGN_WHITE,                  // White resigns
    RESIGN_BLACK,                  // Black resigns
    ON_GOING;                      // Game still in progress

    // Getters
    public boolean whiteWon() {
        return this == WHITE_CHECKMATE || this == RESIGN_WHITE;
    }
    public boolean blackWon() {
        return this == BLACK_CHECKMATE || this == RESIGN_BLACK;
    }

    public boolean isCheckmate() {
        return this == WHITE_CHECKMATE || this == BLACK_CHECKMATE;
    }
    public boolean isDraw() {
        return this == STALEMATE || this == FIFTY_MOVE_RULE || this == THREEFOLD_REPETITION || this == DRAW_AGREEMENT;
    }

    public boolean isOnGoing() {
        return this == ON_GOING;
    }

    @Override
    public String toString() {
        if (whiteWon()) { return "1-0"; }
        if (blackWon()) { return "0-1"; }
        if (isDraw()) { return "1/2-1/2"; }
        return "On Going";
    }
}

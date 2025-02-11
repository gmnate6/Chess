package engine.utils;

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
    ON_GOING                           // Game still in progress
}

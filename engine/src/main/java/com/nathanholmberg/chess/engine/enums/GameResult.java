package com.nathanholmberg.chess.engine.enums;

public enum GameResult {
    WHITE_WON_BY_CHECKMATE,        // White wins by checkmate
    BLACK_WON_BY_CHECKMATE,        // Black wins by checkmate
    STALEMATE,                     // Draw by stalemate
    FIFTY_MOVE_RULE,               // Draw by 50-move rule
    THREEFOLD_REPETITION,          // Draw by move repetition
    WHITE_WON_ON_TIME,             // White won on time
    BLACK_WON_ON_TIME,             // Black won on time
    DRAW_AGREEMENT,                // Draw by mutual agreement
    WHITE_WON_BY_RESIGN,           // White wins by resigns
    BLACK_WON_BY_RESIGN,           // Black wins by resigns
    ON_GOING;                      // Game still in progress

    // Getters
    public boolean whiteWon() {
        return  this == WHITE_WON_BY_CHECKMATE ||
                this == WHITE_WON_BY_RESIGN    ||
                this == WHITE_WON_ON_TIME;
    }

    public boolean blackWon() {
        return  this == BLACK_WON_BY_CHECKMATE ||
                this == BLACK_WON_BY_RESIGN    ||
                this == BLACK_WON_ON_TIME;
    }

    public boolean isCheckmate() {
        return  this == WHITE_WON_BY_CHECKMATE ||
                this == BLACK_WON_BY_CHECKMATE;
    }

    public boolean isDraw() {
        return  this == STALEMATE            ||
                this == FIFTY_MOVE_RULE      ||
                this == THREEFOLD_REPETITION ||
                this == DRAW_AGREEMENT;
    }

    public boolean isOnGoing() {
        return this == ON_GOING;
    }

    public String getScore() {
        if (whiteWon()) { return "1-0"; }
        if (blackWon()) { return "0-1"; }
        if (isDraw()) { return "1/2-1/2"; }
        return "";
    }

    public Color getWinner() {
        if (whiteWon()) { return Color.WHITE; }
        if (blackWon()) { return Color.BLACK; }
        return null;
    }

    public static GameResult fromString(String string) {
        return switch (string) {
            case "WHITE_WON_BY_CHECKMATE" -> WHITE_WON_BY_CHECKMATE;
            case "BLACK_WON_BY_CHECKMATE" -> BLACK_WON_BY_CHECKMATE;
            case "STALEMATE"              -> STALEMATE;
            case "FIFTY_MOVE_RULE"        -> FIFTY_MOVE_RULE;
            case "THREEFOLD_REPETITION"   -> THREEFOLD_REPETITION;
            case "WHITE_WON_ON_TIME"      -> WHITE_WON_ON_TIME;
            case "BLACK_WON_ON_TIME"      -> BLACK_WON_ON_TIME;
            case "DRAW_AGREEMENT"         -> DRAW_AGREEMENT;
            case "WHITE_WON_BY_RESIGN"    -> WHITE_WON_BY_RESIGN;
            case "BLACK_WON_BY_RESIGN"    -> BLACK_WON_BY_RESIGN;
            case "ON_GOING"               -> ON_GOING;
            default -> null;
        };
    }
}

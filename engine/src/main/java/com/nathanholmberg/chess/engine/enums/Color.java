package com.nathanholmberg.chess.engine.enums;

public enum Color {
    WHITE,
    BLACK;

    public Color inverse() {
        return this == WHITE ? BLACK : WHITE;
    }

    public Color random() {
        return (Math.random() < 0.5) ? WHITE : BLACK;
    }

    public String toString() {
        return this == WHITE ? "White" : "Black";
    }

    public static Color fromString(String string) {
        if (string.equalsIgnoreCase("white")) {
            return WHITE;
        } else if (string.equalsIgnoreCase("black")) {
            return BLACK;
        }
        throw new IllegalArgumentException("Invalid Color: " + string);
    }
}

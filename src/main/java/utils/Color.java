package utils;

public enum Color {
    WHITE,
    BLACK;

    public Color inverse() {
        return this == WHITE ? BLACK : WHITE;
    }

    public String toString() {
        return this == WHITE ? "White" : "Black";
    }
}

package engine.pieces;

import engine.board.Board;
import engine.board.Position;
import engine.utils.Move;

import utils.Color;

import java.util.Map;


public abstract class Piece {
    private final Color color;

    // Converting To and From Abbreviation
    private static final Map<Class<? extends Piece>, Character> pieceToChar = Map.of(
            King.class, 'K',
            Queen.class, 'Q',
            Rook.class, 'R',
            Bishop.class, 'B',
            Knight.class, 'N',
            Pawn.class, 'P'
    );
    private static final Map<Character, Class<? extends Piece>> charToPiece = Map.of(
            'K', King.class,
            'Q', Queen.class,
            'R', Rook.class,
            'B', Bishop.class,
            'N', Knight.class,
            'P', Pawn.class
    );
    public static char pieceToChar(Piece piece) {
        char symbol = pieceToChar.get(piece.getClass());
        return piece.getColor() == Color.WHITE ? symbol : Character.toLowerCase(symbol);
    }
    public static Piece charToPiece(char symbol) {
        Class<? extends Piece> pieceClass = charToPiece.get(Character.toUpperCase(symbol));
        if (pieceClass == null) {
            // Invalid input handling
            throw new IllegalArgumentException("Invalid character for piece: " + symbol + ". Valid characters are: K, Q, R, B, N, P.");
        }
        try {
            Color color = Character.isUpperCase(symbol) ? Color.WHITE : Color.BLACK;
            return pieceClass.getDeclaredConstructor(Color.class).newInstance(color);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create piece from character", e);
        }
    }
    public static Piece charToPiece(char symbol, Color color) {
        Class<? extends Piece> pieceClass = charToPiece.get(Character.toUpperCase(symbol));
        if (pieceClass == null) {
            // Invalid input handling
            throw new IllegalArgumentException("Invalid character for piece: " + symbol + ". Valid characters are: K, Q, R, B, N, P.");
        }
        try {
            return pieceClass.getDeclaredConstructor(Color.class).newInstance(color);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create piece from character", e);
        }
    }
    // Converting To and From Abbreviation

    // Constructor
    public Piece(Color color) {
        this.color = color;
    }

    // Getters
    public Color getColor() { return this.color; }

    // To String
    @Override
    public String toString() { return String.valueOf(Piece.pieceToChar(this)); }

    // To Char
    public char toChar() { return Piece.pieceToChar(this); }

    // Get Deep Copy
    public Piece getDeepCopy() {
        return Piece.charToPiece(Piece.pieceToChar(this), getColor());
    }

    /// Move Legality
    public boolean moveLegality(Move move, Board board) {
        /// Cannot capture same color
        Piece finalPositionPiece = board.getPieceAt(move.getFinalPosition());

        // If null
        if (finalPositionPiece == null) {
            return true;
        }

        // Return based on cannot capture same color
        return this.getColor() != finalPositionPiece.getColor();
    }

    // True of path is clear
    protected boolean isPathClear(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();

        // Def some stuff
        int fileDirection = Integer.compare(finalPosition.file(), initialPosition.file());
        int rankDirection = Integer.compare(finalPosition.rank(), initialPosition.rank());

        Position current = initialPosition.move(fileDirection, rankDirection);
        while (!current.equals(finalPosition)) {
            if (board.getPieceAt(current) != null) {
                return false;
            }
            current = current.move(fileDirection, rankDirection);
        }
        return true;
    }
}

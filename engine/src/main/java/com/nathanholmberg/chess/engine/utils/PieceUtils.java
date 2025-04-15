package com.nathanholmberg.chess.engine.utils;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.exceptions.IllegalNotationException;
import com.nathanholmberg.chess.engine.game.Board;
import com.nathanholmberg.chess.engine.pieces.*;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;

import java.util.Map;

public class PieceUtils {
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

    public static Piece getDeepCopy(Piece piece) {
        return charToPiece(piece.toChar());
    }

    public static Piece charToPiece(char symbol) {
        Class<? extends Piece> pieceClass = charToPiece.get(Character.toUpperCase(symbol));
        if (pieceClass == null) {
            // Invalid input handling
            throw new IllegalNotationException("Invalid character for piece: " + symbol + ". Valid characters are: K, Q, R, B, N, P.");
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
            throw new IllegalNotationException("Invalid character for piece: " + symbol + ". Valid characters are: K, Q, R, B, N, P.");
        }
        try {
            return pieceClass.getDeclaredConstructor(Color.class).newInstance(color);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create piece from character", e);
        }
    }

    /**
     * Checks if the path between two positions on the board is clear of any pieces.
     * This method verifies whether there are no blocking pieces along the straight-line path
     * between the `initialPosition` and the `finalPosition`. It checks file and rank movement
     * according to the direction of the move and is primarily used to validate piece movement
     * for linear-moving chess pieces (e.g., rooks, bishops, and queens).
     * This method assumes that:
     * - The move must occur in a straight line (along a rank, file, or diagonal).
     * - The final position is not checked for occupancy, only the intermediate positions.
     */
    public static boolean isPathClear(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

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

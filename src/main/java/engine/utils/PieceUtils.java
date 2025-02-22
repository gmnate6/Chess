package engine.utils;

import engine.game.Board;
import engine.pieces.*;
import utils.Color;

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

    /**
     * Converts a Piece object to its character representation.
     * Uppercase letters represent White pieces, and lowercase letters represent Black pieces.
     *
     * @param piece The Piece to convert.
     * @return The character representing the piece.
     */
    public static char pieceToChar(Piece piece) {
        char symbol = pieceToChar.get(piece.getClass());
        return piece.getColor() == Color.WHITE ? symbol : Character.toLowerCase(symbol);
    }

    /**
     * Creates a deep copy of the given piece.
     *
     * @param piece the Piece to copy
     * @return a new Piece object that is a copy of the input piece
     */
    public static Piece getDeepCopy(Piece piece) {
        return charToPiece(piece.toChar());
    }

    /**
     * Converts a character symbol into a Piece object.
     * <p>
     * This method is case-sensitive:
     * - Uppercase letters (e.g., 'K', 'Q') represent White pieces.
     * - Lowercase letters (e.g., 'k', 'q') represent Black pieces.
     * <p>
     * Example:
     * <pre>
     *     charToPiece('N') -> White Knight
     *     charToPiece('n') -> Black Knight
     * </pre>
     *
     * @param symbol the character that represents a chess piece ('K', 'Q', 'R', 'B', 'N', 'P' or their lowercase forms)
     * @return a Piece object of the correct type and color determined by the symbol's case
     * @throws IllegalArgumentException if the symbol is invalid
     * @throws RuntimeException         if reflection fails to create the Piece object
     */
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

    /**
     * Converts a character symbol into a Piece object of a specific color.
     * <p>
     * This method *ignores* the case of the input symbol. The provided `color`
     * parameter determines the color of the resulting Piece.
     * <p>
     * Example:
     * <pre>
     *     charToPiece('n', Color.WHITE) -> White Knight
     *     charToPiece('N', Color.BLACK) -> Black Knight
     * </pre>
     *
     * @param symbol the character that represents a chess piece ('K', 'Q', 'R', 'B', 'N', 'P' or their lowercase forms)
     *               Case is ignored when determining the piece type.
     * @param color  the color of the resulting piece (e.g., Color.WHITE, Color.BLACK)
     * @return a Piece object of the correct type and specified color
     * @throws IllegalArgumentException if the symbol is invalid
     * @throws RuntimeException         if reflection fails to create the Piece object
     */
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

    /**
     * Checks if the path between two positions on the board is clear of any pieces.
     * <p>
     * This method verifies whether there are no blocking pieces along the straight-line path
     * between the `initialPosition` and the `finalPosition`. It checks file and rank movement
     * according to the direction of the move and is primarily used to validate piece movement
     * for linear-moving chess pieces (e.g., rooks, bishops, and queens).
     * <p>
     * This method assumes that:
     * - The move must occur in a straight line (along a rank, file, or diagonal).
     * - The final position is not checked for occupancy, only the intermediate positions.
     *
     * @param move  the {@link Move} object that contains the `initialPosition` and `finalPosition`.
     *              These positions define the path to check.
     * @param board the {@link Board} object representing the current state of the chessboard.
     *              The `Board` is used to check for pieces at intermediate positions.
     * @return {@code true} if all intermediate positions along the path are empty; {@code false} otherwise.
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

package engine.utils;

import engine.exceptions.IllegalMoveException;
import engine.exceptions.IllegalNotationException;
import engine.game.Board;
import engine.game.Game;
import engine.pieces.King;
import engine.pieces.Pawn;
import engine.pieces.Piece;
import engine.types.Move;
import engine.types.Position;
import utils.Color;

/**
 * Utility class containing helper methods for analyzing and validating moves in the game.
 * This class provides static methods to assess the impact of moves, such as checking for
 * promotions, checks, or checkmates, without modifying the actual game state.
 */
public class MoveUtils {
    public static boolean isCapture(Move move, Game game) {
        Piece pieceToMove = game.board.getPieceAt(move.initialPosition());
        Piece pieceToCapture = game.board.getPieceAt(move.finalPosition());

        // Direct Capture
        if (pieceToCapture != null) {
            return true;
        }

        // En Passant Capture
        if (pieceToMove instanceof Pawn pawn) {
            return pawn.isLegalEnPassant(move, game.board);
        }
        return false;
    }

    /**
     * Determines if the given move causes a promotion.
     *
     * @param move The move that needs to be analyzed.
     * @param game The current game state which contains the board and pieces.
     * @return True if the move results in a promotion (e.g., a pawn reaching the promotion rank),
     *         false otherwise.
     */
    public static boolean causesPromotion(Move move, Game game) {
        Piece pieceToMove = game.board.getPieceAt(move.initialPosition());

        // Must be pawn
        if (!(pieceToMove instanceof Pawn pawn)) {
            return false;
        }
        return pawn.isLegalPromotion(move, game.board);
    }

    /**
     * Determines if the move would put the opposing king in check.
     * This is a private helper method used to assess moves that place
     * the opponent's king under direct threat (check condition).
     *
     * @param move The move to evaluate.
     * @param game The current game state, including the board and pieces.
     * @return True if the move would cause a check, false otherwise.
     */
    private static boolean moveCausesCheck(Move move, Game game) {
        // Create a copy of the board
        Board boardCopy = game.board.getDeepCopy();

        // Apply Move
        boardCopy.executeMove(move);

        // Check if the king is in check after the move
        Color color = game.getTurn().inverse();
        King king = boardCopy.getKing(color);
        Position kingPosition = boardCopy.getKingPosition(color);
        return king.isChecked(kingPosition, boardCopy);
    }

    /**
     * Determines if the move would result in a checkmate.
     * This private method checks if the given move would lead to a game state
     * where the opposing king cannot escape from check, resulting in a checkmate.
     *
     * @param move The move to evaluate.
     * @param game The current game state, including the board and pieces.
     * @return True if the move results in a checkmate, false otherwise.
     */
    private static boolean moveCausesCheckmate(Move move, Game game) {
        Game gameCopy = game.getDeepCopy();

        // Early Return
        if (!gameCopy.isMoveLegal(move)) {
            return false;
        }

        // Move
        gameCopy.move(move);
        return gameCopy.isCheckmate();
    }

    /**
     * Helper method for converting a move to algebraic notation.
     * This method determines any ambiguity in the move's description by analyzing
     * positions of multiple identical pieces that may lead to the same destination.
     *
     * @param move The move to analyze.
     * @param game The current game state to check for potential ambiguities.
     * @return A string describing the ambiguous part of the position, if any.
     */
    private static String getAmbiguity(Move move, Game game) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        Piece pieceToMove = game.board.getPieceAt(initialPosition);
        boolean isCapture = isCapture(move, game);

        // Pawn
        if (pieceToMove instanceof Pawn) {
            if (isCapture) {
                return "" + initialPosition.fileToChar();
            }
            return "";
        }

        // If two identical pieces can move to the same square,
        // the file (letter) or rank (number) of the starting square is included to clarify.
        boolean addFile = false;
        boolean addRank = false;
        for (Position currentPos : game.board.getPiecePositionsByColor(pieceToMove.getColor())) {
            Piece currentPiece = game.board.getPieceAt(currentPos);

            // Skip Same Piece
            if (pieceToMove.equals(currentPiece)) { continue; }
            // Skip Wrong Instance
            if (!(pieceToMove.getClass().equals(currentPiece.getClass()))) { continue; }
            // Skip Illegal Moves
            if (!game.isMoveLegal(new Move(currentPos, finalPosition, '\0'))) { continue; }

            // Do the thing
            if (currentPos.file() == initialPosition.file()) {
                addRank = true;
                continue;
            }
            addFile = true;
        }
        if (addFile && addRank) {
            return initialPosition.toAlgebraic();
        }
        if (addFile) {
            return "" + initialPosition.fileToChar();
        }
        if (addRank) {
            return "" + initialPosition.rankToChar();
        }
        return "";
    }

    /**
     * Resolves ambiguity when decoding algebraic move notation.
     * Ambiguities can arise from multiple pieces of the same type being able to move
     * to the same square. This method determines the correct piece based on additional
     * information provided in the notation.
     *
     * @param ambiguity A string containing ambiguity information (e.g., file or rank).
     * @param pieceChar The character representing the type of the piece (e.g., 'N' for knight).
     * @param finalPosition The target position of the move.
     * @param game The current game state for resolving piece positions.
     * @return The resolved position of the precise piece to move.
     */
    private static Position resolveAmbiguity(String ambiguity, char pieceChar, Position finalPosition, Game game) {
        if (ambiguity.length() > 2) {
            throw new IllegalNotationException("Invalid Algebraic Notation: Ambiguity is too long: '" + ambiguity + "'.");
        }

        // Find Initial Position
        for (Position currentPos : game.board.getPiecePositionsByColor(game.getTurn())) {
            Piece currentPiece = game.board.getPieceAt(currentPos);

            // Skip Wrong Instance
            if (Character.toUpperCase(currentPiece.toChar()) != Character.toUpperCase(pieceChar)) { continue; }
            // Skip Illegal Moves
            if (!game.isMoveLegal(new Move(currentPos, finalPosition, 'Q'))) { continue; }

            // No Ambiguity
            if (ambiguity.isEmpty()) {
                return currentPos;
            }

            // Some Ambiguity
            if (ambiguity.length() == 1) {
                // File Ambiguity
                if (ambiguity.charAt(0) == currentPos.rankToChar()) {
                    return currentPos;
                }

                // Rank Ambiguity
                if (ambiguity.charAt(0) == currentPos.fileToChar()) {
                    return currentPos;
                }
            }

            // All Ambiguity
            if (ambiguity.length() == 2) {
                // Check if currentPos is it
                if (currentPos.toAlgebraic().equals(ambiguity)) {
                    return currentPos;
                }
            }
        }
        throw new IllegalNotationException("Ambiguous algebraic notation: cannot resolve initial position from ambiguity: '" + ambiguity + "'.");
    }

    /**
     * Creates a move representing a castling action.
     * Castling is a special move in chess where the king moves two spaces towards a rook,
     * and the rook moves to the square adjacent to the king. This method handles both
     * king-side and queen-side castling.
     *
     * @param game The current game state.
     * @param isKingSide True if the castling move is king-side, false for queen-side.
     * @return A Move object representing the castling action.
     */
    private static Move createCastlingMove(Game game, boolean isKingSide) {
        Position initialPosition = game.board.getKingPosition(game.getTurn());
        Position finalPosition = initialPosition.move(isKingSide ? 2 : -2, 0);
        return new Move(initialPosition, finalPosition, '\0');
    }

    /**
     * Converts an algebraic chess move notation (e.g., "Qxe4#") into a Move object.
     * The method parses the given notation, interprets the move, and constructs
     * a Move instance reflecting the player's action on the board.
     *
     * @param notation The algebraic notation of the move.
     * @param game The current game state to resolve the move's context.
     * @return A Move object representing the action described by the notation.
     */
    public static Move fromAlgebraic(String notation, Game game) {
        Position initialPosition;
        Position finalPosition;
        char promotionPiece = '\0';

        // Original Notation
        String originalNotation = notation;

        // Ensure notation is not null or empty
        if (notation == null || notation.isEmpty()) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'");
        }

        // Handle Extras
        boolean isCapture = notation.contains("x");
        boolean causesCheck = notation.contains("+");
        boolean causesCheckmate = notation.contains("#");
        boolean isPromotion = notation.contains("=");
        notation = notation.replaceAll("[+#x]", "");

        // Handle castling
        if (notation.equals("O-O")) {
            return createCastlingMove(game, true);
        } else if (notation.equals("O-O-O")) {
            return createCastlingMove(game, false);
        }

        // Handle Promotion
        if (isPromotion) {
            int promotionIndex = notation.indexOf('=');

            // Update Promotion Piece
            promotionPiece = notation.charAt(promotionIndex + 1);

            // Remove Promotion Part
            notation = notation.substring(0, promotionIndex);
        }

        // Get Final Position
        String finalPositionString = notation.substring(notation.length() - 2);
        finalPosition = Position.fromAlgebraic(finalPositionString);

        // Remove Final Position from notation
        notation = notation.substring(0, notation.length() - 2);

        // Get Piece To Move Char
        char pieceToMoveChar = (notation.isEmpty() || !Character.isUpperCase(notation.charAt(0)))
                ? 'P'
                : Character.toUpperCase(notation.charAt(0));

        // Remove piece character from notation if it's not a pawn
        if (pieceToMoveChar != 'P') {
            notation = notation.substring(1);
        }

        // Resolve Ambiguity
        try {
            initialPosition = resolveAmbiguity(notation, pieceToMoveChar, finalPosition, game);
        } catch (IllegalNotationException e) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. ");
        }

        // Validate the move
        Move move = new Move(initialPosition, finalPosition, promotionPiece);
        if (!game.isMoveLegal(move)) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. Move is not legal.");
        }

        // Check for extras
        if (isCapture ^ isCapture(move, game)) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. Capture symbol does not match move.");
        }
        if ((causesCheck ^ moveCausesCheck(move, game)) && !causesCheckmate) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. Check symbol does not match move.");
        }
        if (causesCheckmate ^ moveCausesCheckmate(move, game)) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. Checkmate symbol does not match move.");
        }

        // Return Move
        return move;
    }

    /**
     * Converts a Move object into algebraic chess notation.
     * This method generates a human-readable notation based on the move's details,
     * such as piece type, starting position, and destination. It also considers
     * special moves like checks, captures, or castling.
     *
     * @param move The Move object to convert.
     * @param game The current game state for interpreting the move's context.
     * @return A string representing the move in standard algebraic notation.
     */
    public static String toAlgebraic(Move move, Game game) {
        StringBuilder sb = new StringBuilder();
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        char promotionPiece = move.promotionPiece();
        Piece pieceToMove = game.board.getPieceAt(initialPosition);
        boolean isCapture = isCapture(move, game);

        // Early Check
        if (!game.isMoveLegal(move)) {
            throw new IllegalMoveException("Illegal Move: Make sure to call toAlgebraic() before making move.");
        }
        assert (pieceToMove != null);

        // Castling
        if (pieceToMove instanceof King) {
            // Short
            if (((King) pieceToMove).isCastleAttempt(move, true)) {
                return "O-O";
            }
            // Long
            if (((King) pieceToMove).isCastleAttempt(move, false)) {
                return "O-O-O";
            }
        }

        // Add Piece Char
        if (!(pieceToMove instanceof Pawn)) {
            sb.append(Character.toUpperCase(pieceToMove.toChar()));
        }

        // Add Initial Position
        sb.append(getAmbiguity(move, game));

        // Add Capture
        if (isCapture) {
            sb.append("x");
        }

        // Add Final Position
        sb.append(finalPosition.toAlgebraic());

        // Add Promotion
        if (promotionPiece != '\0') {
            sb.append("=").append(promotionPiece);
        }

        // Add Check and Checkmate
        if (moveCausesCheckmate(move, game)) {
            sb.append("#");
        } else if (moveCausesCheck(move, game)) {
            sb.append("+");
        }

        // Return sb
        return sb.toString();
    }

    /**
     * Converts long algebraic chess notation (e.g., "e2-e4") into a Move object.
     * The method parses the long algebraic description and creates a Move instance
     * that captures the details of the action on the chessboard.
     *
     * @param notation The long algebraic notation of the move.
     * @return A Move object representing the described action.
     */
    public static Move fromLongAlgebraic(String notation, Game game) {
        if (notation.length() < 4 || notation.length() > 5) {
            throw new IllegalNotationException("Invalid Long Algebraic Notation: " + notation);
        }

        // Get Sub Strings
        String from = notation.substring(0, 2);
        String to = notation.substring(2, 4);
        char promotion = notation.length() == 5 ? notation.charAt(4) : '\0';

        // Try to make Positions
        try {
            Position initialPosition = Position.fromAlgebraic(from);
            Position finalPosition = Position.fromAlgebraic(to);

            // Create Move
            Move move = new Move(initialPosition, finalPosition, promotion);

            // Check for promotion
            if (promotion == '\0' && causesPromotion(move, game)) {
                throw new IllegalNotationException("Invalid Long Algebraic Notation: '" + notation + "'. Move causes promotion and no promotion piece not specified.\n" +
                        "To specify a promotion piece, just add the piece char after the destination square. (e.g., a7a8Q)");
            }
            if (promotion != '\0' && !causesPromotion(move, game)) {
                throw new IllegalNotationException("Invalid Long Algebraic Notation: '" + notation + "'. Move does not cause promotion but a promotion piece was specified.\n" +
                        "To remove a promotion piece, just omit the piece char after the destination square. (e.g., a7a8)");
            }

            // Make and Return Move
            return new Move(initialPosition, finalPosition, promotion);
        } catch (Exception e) {
            throw new IllegalNotationException("Invalid Long Algebraic Notation: " + notation + "\nError Message: " + e.getMessage());
        }
    }

    /**
     * Converts a Move object into long algebraic chess notation.
     * Long algebraic notation includes both the starting and ending squares of the move
     * explicitly, such as "e2-e4". This method generates that format based on the Move's details.
     *
     * @param move The Move object to convert.
     * @return A string representing the move in long algebraic notation.
     */
    public static String toLongAlgebraic(Move move) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        char promotionPiece = move.promotionPiece();
        return initialPosition.toAlgebraic() + finalPosition.toAlgebraic() + (promotionPiece == '\0' ? "" : promotionPiece);
    }
}

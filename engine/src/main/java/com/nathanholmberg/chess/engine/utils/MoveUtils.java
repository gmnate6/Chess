package com.nathanholmberg.chess.engine.utils;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.exceptions.IllegalMoveException;
import com.nathanholmberg.chess.engine.exceptions.IllegalNotationException;
import com.nathanholmberg.chess.engine.game.Board;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.pieces.King;
import com.nathanholmberg.chess.engine.pieces.Pawn;
import com.nathanholmberg.chess.engine.pieces.Piece;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;

public class MoveUtils {
    public static boolean isCapture(Move move, ChessGame chessGame) {
        Piece pieceToMove = chessGame.board.getPieceAt(move.initialPosition());
        Piece pieceToCapture = chessGame.board.getPieceAt(move.finalPosition());

        // Direct Capture
        if (pieceToCapture != null) {
            return true;
        }

        // En Passant Capture
        if (pieceToMove instanceof Pawn pawn) {
            return pawn.isLegalEnPassant(move, chessGame.board);
        }
        return false;
    }

    public static boolean causesPromotion(Move move, ChessGame chessGame) {
        Piece pieceToMove = chessGame.board.getPieceAt(move.initialPosition());

        // Must be pawn
        if (!(pieceToMove instanceof Pawn pawn)) {
            return false;
        }
        return pawn.isLegalPromotion(move, chessGame.board);
    }

    public static boolean isCastlingMove(Move move, ChessGame chessGame) {
        Piece pieceToMove = chessGame.board.getPieceAt(move.initialPosition());

        // Must be king
        if (!(pieceToMove instanceof King king)) {
            return false;
        }

        // Attempted castle
        return king.isCastleAttempt(move);
    }

    public static boolean causesCheck(Move move, ChessGame chessGame) {
        // Create a copy of the board
        Board boardCopy = chessGame.board.getDeepCopy();

        // Apply Move
        boardCopy.executeMove(move);

        // Check if the king is in check after the move
        Color color = chessGame.getTurn().inverse();
        King king = boardCopy.getKing(color);
        Position kingPosition = boardCopy.getKingPosition(color);
        return king.isChecked(kingPosition, boardCopy);
    }

    public static boolean causesCheckmate(Move move, ChessGame chessGame) {
        ChessGame chessGameCopy = chessGame.getDeepCopy();

        // Early Return
        if (!chessGameCopy.isMoveLegal(move)) {
            return false;
        }

        // Move
        chessGameCopy.move(move);
        return chessGameCopy.isCheckmate();
    }

    private static String getAmbiguity(Move move, ChessGame chessGame) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        Piece pieceToMove = chessGame.board.getPieceAt(initialPosition);
        boolean isCapture = isCapture(move, chessGame);

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
        for (Position currentPos : chessGame.board.getPiecePositionsByColor(pieceToMove.getColor())) {
            Piece currentPiece = chessGame.board.getPieceAt(currentPos);

            // Skip Same Piece
            if (pieceToMove.equals(currentPiece)) { continue; }
            // Skip Wrong Instance
            if (!(pieceToMove.getClass().equals(currentPiece.getClass()))) { continue; }
            // Skip Illegal Moves
            if (!chessGame.isMoveLegal(new Move(currentPos, finalPosition, '\0'))) { continue; }

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

    private static Position resolveAmbiguity(String ambiguity, char pieceChar, Position finalPosition, ChessGame chessGame) {
        if (ambiguity.length() > 2) {
            throw new IllegalNotationException("Invalid Algebraic Notation: Ambiguity is too long: '" + ambiguity + "'.");
        }

        // Find Initial Position
        for (Position currentPos : chessGame.board.getPiecePositionsByColor(chessGame.getTurn())) {
            Piece currentPiece = chessGame.board.getPieceAt(currentPos);

            // Skip Wrong Instance
            if (Character.toUpperCase(currentPiece.toChar()) != Character.toUpperCase(pieceChar)) { continue; }
            // Skip Illegal Moves
            if (!chessGame.isMoveLegal(new Move(currentPos, finalPosition, 'Q'))) { continue; }

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

    private static Move createCastlingMove(ChessGame chessGame, boolean isKingSide) {
        Position initialPosition = chessGame.board.getKingPosition(chessGame.getTurn());
        Position finalPosition = initialPosition.move(isKingSide ? 2 : -2, 0);
        return new Move(initialPosition, finalPosition, '\0');
    }

    public static Move fromAlgebraic(String notation, ChessGame chessGame) {
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
            return createCastlingMove(chessGame, true);
        } else if (notation.equals("O-O-O")) {
            return createCastlingMove(chessGame, false);
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
            initialPosition = resolveAmbiguity(notation, pieceToMoveChar, finalPosition, chessGame);
        } catch (IllegalNotationException e) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. ");
        }

        // Validate the move
        Move move = new Move(initialPosition, finalPosition, promotionPiece);
        if (!chessGame.isMoveLegal(move)) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. Move is not legal.");
        }

        // Check for extras
        if (isCapture ^ isCapture(move, chessGame)) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. Capture symbol does not match move.");
        }
        if ((causesCheck ^ causesCheck(move, chessGame)) && !causesCheckmate) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. Check symbol does not match move.");
        }
        if (causesCheckmate ^ causesCheckmate(move, chessGame)) {
            throw new IllegalNotationException("Illegal Algebraic Notation: '" + originalNotation + "'. Checkmate symbol does not match move.");
        }

        // Return Move
        return move;
    }

    public static String toAlgebraic(Move move, ChessGame chessGame) {
        StringBuilder sb = new StringBuilder();
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        char promotionPiece = move.promotionPiece();
        Piece pieceToMove = chessGame.board.getPieceAt(initialPosition);
        boolean isCapture = isCapture(move, chessGame);

        // Early Check
        if (!chessGame.isMoveLegal(move)) {
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
        sb.append(getAmbiguity(move, chessGame));

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
        if (causesCheckmate(move, chessGame)) {
            sb.append("#");
        } else if (causesCheck(move, chessGame)) {
            sb.append("+");
        }

        // Return sb
        return sb.toString();
    }

    public static Move fromLongAlgebraic(String notation, ChessGame chessGame) {
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
            if (promotion == '\0' && causesPromotion(move, chessGame)) {
                throw new IllegalNotationException("Invalid Long Algebraic Notation: '" + notation + "'. Move causes promotion and no promotion piece not specified.\n" +
                        "To specify a promotion piece, just add the piece char after the destination square. (e.g., a7a8Q)");
            }
            if (promotion != '\0' && !causesPromotion(move, chessGame)) {
                throw new IllegalNotationException("Invalid Long Algebraic Notation: '" + notation + "'. Move does not cause promotion but a promotion piece was specified.\n" +
                        "To remove a promotion piece, just omit the piece char after the destination square. (e.g., a7a8)");
            }

            // Make and Return Move
            return new Move(initialPosition, finalPosition, promotion);
        } catch (Exception e) {
            throw new IllegalNotationException("Invalid Long Algebraic Notation: " + notation + "\nError Message: " + e.getMessage());
        }
    }

    public static String toLongAlgebraic(Move move) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        char promotionPiece = move.promotionPiece();
        return initialPosition.toAlgebraic() + finalPosition.toAlgebraic() + (promotionPiece == '\0' ? "" : promotionPiece);
    }
}

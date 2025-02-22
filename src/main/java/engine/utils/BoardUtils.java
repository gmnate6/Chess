package engine.utils;

import engine.game.Board;
import engine.pieces.*;
import utils.Color;

/**
 * The `BoardUtils` class provides utility methods to perform various operations
 * on a chess board, such as creating a deep copy of the board and applying moves.
 * Its functionality includes deep-copying board states and executing moves while
 * handling chess rules and mechanics such as en passant, castling, and promotion.
 * This class serves as a helper for managing the state of a chess game programmatically.
 */
public class BoardUtils {
    /**
     * Creates a deep copy of the board, preserving the state of the board including:
     * - The positions and states of all pieces.
     * - Castling rights for both players.
     * - The en passant position (if available).
     * The resulting deep copy is independent of the original board, meaning changes
     * made to one will not affect the other.
     *
     * @param board The `Board` object representing the current state of the game.
     * @return A new `Board` object that is a deep copy of the provided board, including
     *         all the pieces, castling rights, and en passant state.
     */
    public static Board getDeepCopy(Board board) {
        Board copy = Board.getEmptyBoard();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position position = new Position(file, rank);
                Piece piece = board.getPieceAt(position);
                if (piece != null) {
                    copy.setPieceAt(position, piece.getDeepCopy());
                }
            }
        }
        copy.setEnPassantPosition(board.getEnPassantPosition());
        copy.setCastlingRights(board.getCastlingRights().getDeepCopy());
        return copy;
    }

    /**
     * Executes a specified chess move on the given board, making all relevant changes
     * to the board state. This includes handling the following special chess rules:
     * - **En Passant**: Captures en passant pawns and updates the en passant position.
     * - **Pawn Promotion**: Promotes pawns to a specified piece (default: Queen).
     * - **Castling**: Moves the rook in addition to the king and updates castling rights.
     * - **Castling Rights**: Updates castling rights when rooks or the king move.
     * It updates the board state accordingly and ensures the integrity of rules at every step.
     *
     * @param move  The `Move` object that describes the chess move to execute. It contains:
     *              - The initial position of the moving piece.
     *              - The final position of the moving piece.
     *              - Optional promotion details if the move involves pawn promotion.
     * @param board The `Board` object representing the current state of the chess board.
     * @throws IllegalStateException If there is no piece at the specified initial position.
     */
    public static void executeMove(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        Piece pieceToMove = board.getPieceAt(initialPosition);

        // No Piece To Move
        if (pieceToMove == null) {
            throw new IllegalStateException("No piece at initial position: " + initialPosition);
        }

        // Pawn Weirdness
        if (pieceToMove instanceof Pawn) {
            // If enPassant
            if (((Pawn) pieceToMove).isLegalEnPassant(move, board)) {
                // Remove En Passanted Pawn
                board.setPieceAt(board.getEnPassantPosition(), null);
            }

            // Update enPassantPosition
            if (((Pawn) pieceToMove).isLegalDouble(move, board)) {
                board.setEnPassantPosition(finalPosition);
            } else {
                board.setEnPassantPosition(null);
            }

            // Promotion
            if (finalPosition.rank() == 0 || finalPosition.rank() == 7) {
                pieceToMove = PieceUtils.charToPiece(move.promotionPiece(), pieceToMove.getColor());
            }
        } else {
            board.setEnPassantPosition(null);
        }

        // King Weirdness
        if (pieceToMove instanceof King) {
            // If Castle King Side
            if (((King) pieceToMove).isCastleAttempt(move, true)) {
                Position rookPosition = initialPosition.move(3, 0);
                Piece rookPiece = board.getPieceAt(rookPosition);

                // Place Rook
                board.setPieceAt(initialPosition.move(1, 0), rookPiece);

                // Remove Old Rook
                board.setPieceAt(rookPosition, null);
            }

            // If Castle Queen Side
            if (((King) pieceToMove).isCastleAttempt(move, false)) {
                Position rookPosition = initialPosition.move(-4, 0);
                Piece rookPiece = board.getPieceAt(rookPosition);

                // Place Rook
                board.setPieceAt(initialPosition.move(-1, 0), rookPiece);

                // Remove Old Rook
                board.setPieceAt(rookPosition, null);
            }

            // Update CastlingRights
            if (pieceToMove.getColor() == Color.WHITE) {
                board.getCastlingRights().setWhiteKingSide(false);
                board.getCastlingRights().setWhiteQueenSide(false);
            } else {
                board.getCastlingRights().setBlackKingSide(false);
                board.getCastlingRights().setBlackQueenSide(false);
            }
        }

        // Rook Weirdness
        if (pieceToMove instanceof Rook) {
            // Update CastlingRights
            if (pieceToMove.getColor() == Color.WHITE) {
                // White
                if (initialPosition.toAlgebraic().equals("a1")) {
                    board.getCastlingRights().setWhiteQueenSide(false);
                }
                if (initialPosition.toAlgebraic().equals("h1")) {
                    board.getCastlingRights().setWhiteKingSide(false);
                }
            } else {
                // Black
                if (initialPosition.toAlgebraic().equals("a8")) {
                    board.getCastlingRights().setBlackQueenSide(false);
                }
                if (initialPosition.toAlgebraic().equals("h8")) {
                    board.getCastlingRights().setBlackKingSide(false);
                }
            }
        }

        // Update Board
        board.setPieceAt(initialPosition, null);
        board.setPieceAt(finalPosition, pieceToMove);
    }
}

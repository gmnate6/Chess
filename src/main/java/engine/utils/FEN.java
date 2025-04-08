package engine.utils;

import engine.exceptions.IllegalNotationException;
import engine.game.Board;
import engine.game.Game;
import engine.game.ChessTimer;
import engine.pieces.Piece;
import engine.types.CastlingRights;
import engine.types.Position;
import utils.Color;

/**
 * Utility class for working with the Forsyth-Edwards Notation (FEN).
 * FEN is a standard notation for representing a chessboard's current state,
 * including board setup, turn, castling rights, en passant target, and more.
 * This class provides methods to interpret FEN strings and convert game states
 * into FEN format.
 */
public class FEN {
    /**
     * Parses the board part of a FEN string and converts it into a <code>Board</code> object.
     * Each rank in the FEN string is interpreted, and pieces are placed accordingly.
     *
     * @param boardFEN A part of a FEN string representing the board setup.
     * @return A <code>Board</code> object populated based on the given FEN string.
     */
    private static Board getBoard(String boardFEN) {
        String[] ranks = boardFEN.split("/");
        Board board = Board.getEmptyBoard();

        // Check for 8 ranks
        if (ranks.length != 8) {
            throw new IllegalNotationException("Illegal FEN: Board must be 8 ranks");
        }

        // Check for Kings
        if (!boardFEN.contains("K") || !boardFEN.contains("k")) {
            throw new IllegalNotationException("Illegal FEN: Both kings (K and k) MUST be on the board.");
        }

        // Loop Through Board
        for (int rank = 0; rank < 8; rank++) {
            int file = 0;
            for (char c : ranks[7 - rank].toCharArray()) {
                // Skip if number
                if (Character.isDigit(c)) {
                    file += (c - '0');
                    continue;
                }

                if (file >= 8) {
                    throw new IllegalNotationException("Illegal FEN: Board must be 8 ranks wide.");
                }

                // Set Piece On Board
                Position pos = new Position(file, rank);
                try {
                    board.setPieceAt(pos, PieceUtils.charToPiece(c));
                } catch (IllegalArgumentException e) {
                    throw new IllegalNotationException("Illegal FEN: Invalid piece character '" + c + "' at position " + pos.toAlgebraic() + ".");
                }

                // No Pawns on edge
                if ((rank == 0 || rank == 7) && (c == 'P' || c == 'p')) {
                    throw new IllegalNotationException("Illegal FEN: Pawns cannot be on the 1st or 8th ranks.");
                }

                // Move to Next File
                file++;
            }
            if (file != 8) {
                throw new IllegalNotationException("Illegal FEN: Board must be 8 ranks wide.");
            }
        }

        // Return Board
        return board;
    }

    /**
     * Parses an entire FEN string and constructs a <code>Game</code> object.
     * This includes information such as the board layout, current turn, castling rights,
     * and en passant targets. Optionally includes a timer state if provided.
     *
     * @param fen The FEN string representing the game's current state.
     * @param chessTimer An optional <code>Timer</code> object for tracking elapsed time (can be null).
     * @return A <code>Game</code> object reconstructed from the given FEN string.
     */
    public static Game getGame(String fen, ChessTimer chessTimer) {
        Board board;
        Color currentPlayer;
        int halfMoveClock;
        int fullMoveNumber;

        // Early Throw
        if (fen == null || fen.isBlank()) {
            throw new IllegalNotationException("Illegal FEN: FEN string cannot be null or blank.");
        }

        // Break into parts
        String[] fenParts = fen.split(" ");

        // Early Throw
        if (fenParts.length != 6) {
            throw new IllegalNotationException("Illegal FEN: FEN string must contain exactly 6 parts.");
        }

        /// Construct Board
        String boardFEN = fenParts[0];
        board = getBoard(boardFEN);

        /// Construct Current Player
        String currentPlayerFEN = fenParts[1];

        // Early Throw
        if (currentPlayerFEN.isBlank() || currentPlayerFEN.length() > 1) {
            throw new IllegalNotationException("Illegal FEN: Current Player must be either 'w' or 'b'.");
        }
        currentPlayerFEN = currentPlayerFEN.toLowerCase();
        if (!currentPlayerFEN.equals("w") && !currentPlayerFEN.equals("b")) {
            throw new IllegalNotationException("Illegal FEN: Current Player must either be 'w' or 'b'.");
        }
        currentPlayer = currentPlayerFEN.equals("w") ? Color.WHITE : Color.BLACK;

        // Cannot be left in check
        if (board.isKingInCheck(currentPlayer.inverse())) {
            throw new IllegalNotationException("Illegal FEN: Other Player cannot be left in check.");
        }

        /// Construct Castling Rights
        String castlingRightsFEN = fenParts[2];
        CastlingRights castlingRights = new CastlingRights();

        // Set Castling Rights
        castlingRights.setWhiteQueenSide(castlingRightsFEN.contains("Q"));
        castlingRights.setWhiteKingSide(castlingRightsFEN.contains("K"));
        castlingRights.setBlackQueenSide(castlingRightsFEN.contains("q"));
        castlingRights.setBlackKingSide(castlingRightsFEN.contains("k"));

        // Verify Castling Rights
        castlingRights.verifyRights(board);

        // Add Castling Rights to Board
        board.setCastlingRights(castlingRights);

        /// Construct En Passant Position
        String enPassantPositionFEN = fenParts[3];

        // Early Throw
        if (enPassantPositionFEN.isBlank() || enPassantPositionFEN.length() > 2) {
            throw new IllegalNotationException("Illegal FEN: Current Player must be either 'w' or 'b'.");
        }

        // get En Passant Position
        Position enPassantPosition;
        if (enPassantPositionFEN.equals("-")) {
            enPassantPosition = null;
        } else {
            try {
                enPassantPosition = Position.fromAlgebraic(enPassantPositionFEN);
            } catch (IllegalArgumentException e) {
                throw new IllegalNotationException("Illegal FEN: Invalid en passant target position '" + enPassantPositionFEN + "'.");
            }
        }

        // Add enPassantPosition to Board
        board.setEnPassantPosition(enPassantPosition);

        /// Construct Half Move Clock
        String halfMoveClockFEN = fenParts[4];
        try {
            halfMoveClock = Integer.parseInt(halfMoveClockFEN);
        } catch (NumberFormatException e) {
            throw new IllegalNotationException("Illegal FEN: Invalid half move clock '" + halfMoveClockFEN + "'.");
        }

        // Constrain Half Move Clock
        if (halfMoveClock < 0 || halfMoveClock > 100) {
            throw new IllegalNotationException("Illegal FEN: Half move clock must be between 0 and 100.");
        }

        /// Construct Full Move Number
        String fullMoveNumberFEN = fenParts[5];
        try {
            fullMoveNumber = Integer.parseInt(fullMoveNumberFEN);
        } catch (NumberFormatException e) {
            throw new IllegalNotationException("Illegal FEN: Invalid full move number '" + fullMoveNumberFEN + "'.");
        }

        // Constrain Full Move Number
        if (fullMoveNumber <= 0) {
            throw new IllegalNotationException("Illegal FEN: Full move number must be greater than 0.");
        }

        // Construct and return the FEN object.
        return new Game(board, currentPlayer, halfMoveClock, fullMoveNumber, chessTimer);
    }

    /**
     * Generates the FEN string for a <code>Game</code>, which includes the board state
     * and the player's turn. This method does not include additional details such as
     * castling rights, en passant, or move counters.
     *
     * @param game The <code>Game</code> object to convert to FEN.
     * @return A FEN string that includes the board state and the turn of the current player.
     */
    public static String getFENBoardAndTurn(Game game) {
        StringBuilder fen = new StringBuilder();

        // Construct Board
        for (int rank = 7; rank >= 0; rank--) {
            int emptyCount = 0;

            // Loop through files
            for (int file = 0; file < 8; file++) {
                Position currentPosition = new Position(file, rank);
                Piece currentPiece = game.board.getPieceAt(currentPosition);

                if (currentPiece == null) {
                    // Count Empty Spaces
                    emptyCount++;
                } else {
                    // Append Number of empty Spaces if any
                    if (emptyCount > 0) {
                        fen.append(emptyCount);
                        emptyCount = 0;
                    }

                    // Append Piece Representation
                    fen.append(currentPiece);
                }
            }

            // Append Remaining Empty Spaces
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }

            // Add '/' in between ranks
            if (rank > 0) {
                fen.append('/');
            }
        }

        // Construct Current Player
        fen.append(" ").append(game.getTurn() == Color.WHITE ? "w" : "b");
        return fen.toString();
    }

    /**
     * Converts the entire state of a <code>Game</code> object into a full FEN string.
     * The generated string includes board layout, active color, castling rights,
     * en passant target square, halfmove clock, and fullmove number.
     *
     * @param game The <code>Game</code> object to convert to FEN.
     * @return A complete FEN string representing the state of the given game.
     */
    public static String getFEN(Game game) {
        StringBuilder fen = new StringBuilder();

        // Construct Board
        fen.append(getFENBoardAndTurn(game));

        // Construct Castling Rights
        fen.append(" ");

        CastlingRights castlingRights = game.board.getCastlingRights();
        if (castlingRights.isWhiteKingSide()) { fen.append("K"); }
        if (castlingRights.isWhiteQueenSide()) { fen.append("Q"); }
        if (castlingRights.isBlackKingSide()) { fen.append("k"); }
        if (castlingRights.isBlackQueenSide()) { fen.append("q"); }
        // If no castling rights add "-"
        if (castlingRights.isNone()) { fen.append("-"); }

        // Construct En Passant Position
        Position enPassantPosition = game.board.getEnPassantPosition();
        fen.append(" ");
        if (enPassantPosition == null) {
            fen.append("-");
        } else {
            fen.append(enPassantPosition.toAlgebraic());
        }

        // Construct Half Move Clock
        fen.append(" ").append(game.getHalfMoveClock());

        // Construct Full Move Number
        fen.append(" ").append(game.getFullMoveNumber());

        // Return
        return fen.toString();
    }
}

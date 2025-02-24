package engine.utils;

import engine.pieces.Piece;
import engine.game.Board;

import engine.types.CastlingRights;
import engine.types.Position;
import utils.Color;

public final class FEN {
    private final Board board;
    private final Color currentPlayer;
    private final CastlingRights castlingRights;
    private final Position enPassantPosition;
    private final int halfMoveClock;
    private final int fullMoveNumber;

    /**
     * Constructs a new FEN object with the given board state and additional details.
     *
     * @param board The current state of the chess board.
     * @param currentPlayer The player whose turn it is (White or Black).
     * @param halfMoveClock The halfmove counter for the 50-move rule.
     * @param fullMoveNumber The current full move number.
     */
    public FEN(Board board, Color currentPlayer, int halfMoveClock, int fullMoveNumber) {
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.castlingRights = board.getCastlingRights();
        this.enPassantPosition = board.getEnPassantPosition();
        this.halfMoveClock = halfMoveClock;
        this.fullMoveNumber = fullMoveNumber;
    }

    /**
     * Creates a `FEN` object by parsing the given FEN string.
     *
     * @param fen The FEN string representing the board state.
     * @return A new `FEN` object representing the parsed board state.
     * @throws RuntimeException If the FEN string is invalid, such as missing kings.
     */
    public static FEN fromFEN(String fen) {
        String[] fenParts = fen.split(" ");

        // Construct Board
        String boardFEN = fenParts[0];
        String[] ranks = boardFEN.split("/");
        Board board = Board.getEmptyBoard();

        // Check for Kings
        if (!boardFEN.contains("K") || !boardFEN.contains("k")) {
            throw new RuntimeException("Illegal FEN: Both kings (K and k) MUST be on the board.");
        }

        // Check for 8 ranks
        if (ranks.length != 8) {
            throw new RuntimeException("Illegal FEN: Board must be 8 ranks");
        }

        // Loop Through Board
        for (int rank = 0; rank < 8; rank++) {
            int file = 0;
            for (char c : ranks[7 - rank].toCharArray()) {
                if (Character.isDigit(c)) {
                    file += (c - '0');
                } else {
                    // Set Piece On Board
                    Position pos = new Position(file, rank);
                    board.setPieceAt(pos, PieceUtils.charToPiece(c));

                    // No Pawns on edge
                    if (rank == 0 || rank == 7) {
                        if (c == 'P' || c == 'p') {
                            throw new RuntimeException("Illegal FEN: Pawns cannot be placed on the edge of the board.");
                        }
                    }

                    // Move to Next File
                    file++;
                }
            }
        }

        // Construct Current Player
        String currentPlayerFEN = fenParts[1];
        if (!currentPlayerFEN.equalsIgnoreCase("w") && !currentPlayerFEN.equalsIgnoreCase("b")) {
            throw new RuntimeException("Illegal FEN: Current Player must either be 'w' or 'b'.");
        }
        Color currentPlayer = currentPlayerFEN.equalsIgnoreCase("w") ? Color.WHITE : Color.BLACK;

        // Cannot be left in check
        if (board.isKingInCheck(currentPlayer.inverse())) {
            throw new RuntimeException("Illegal FEN: Other Player cannot be left in check.");
        }

        // Construct Castling Rights
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

        // Construct En Passant Position
        String enPassantPositionFEN = fenParts[3];
        Position enPassantPosition;
        if (enPassantPositionFEN.equals("-")) {
            enPassantPosition = null;
        } else {
            try {
                enPassantPosition = Position.fromAlgebraic(enPassantPositionFEN);
            } catch (IllegalArgumentException e) {
                enPassantPosition = null;
            }
        }

        // Add enPassantPosition to Board
        board.setEnPassantPosition(enPassantPosition);

        // Construct Half Move Clock
        String halfMoveClockFEN = fenParts[4];
        int halfMoveClock;
        try {
            halfMoveClock = Integer.parseInt(halfMoveClockFEN);
        } catch (NumberFormatException e) {
            halfMoveClock = 0;
        }

        // Construct Full Move Number
        String fullMoveNumberFEN = fenParts[5];
        int fullMoveNumber;
        try {
            fullMoveNumber = Integer.parseInt(fullMoveNumberFEN);
        } catch (NumberFormatException e) {
            fullMoveNumber = 0;
        }

        // Construct and return the FEN object.
        return new FEN(board, currentPlayer, halfMoveClock, fullMoveNumber);
    }

    /**
     * Converts the current board to its FEN string representation.
     *
     * @return The FEN string representing the board state.
     */
    public String toFENBoardAndTurn() {
        StringBuilder fen = new StringBuilder();

        // Construct Board
        for (int rank = 7; rank >= 0; rank--) {
            int emptyCount = 0;

            // Loop through files
            for (int file = 0; file < 8; file++) {
                Position currentPosition = new Position(file, rank);
                Piece currentPiece = board.getPieceAt(currentPosition);

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
                    fen.append(currentPiece.toString());
                }
            }

            // Append Remaining Empty Spaces
            if (emptyCount > 0) {
                fen.append(emptyCount);
            }

            // Add '/' inbetween ranks
            if (rank > 0) {
                fen.append('/');
            }
        }

        // Construct Current Player
        fen.append(" ").append(this.currentPlayer == Color.WHITE ? "w" : "b");

        return fen.toString();
    }

    /**
     * Converts the current game state to its FEN string representation.
     *
     * @return The FEN string representing the game state.
     */
    public String toFEN() {
        StringBuilder fen = new StringBuilder();

        // Construct Board
        fen.append(this.toFENBoardAndTurn());

        // Construct Castling Rights
        fen.append(" ");

        if (castlingRights.isWhiteKingSide()) { fen.append("K"); }
        if (castlingRights.isWhiteQueenSide()) { fen.append("Q"); }
        if (castlingRights.isBlackKingSide()) { fen.append("k"); }
        if (castlingRights.isBlackQueenSide()) { fen.append("q"); }
        // If no castling rights add "-"
        if (castlingRights.isNone()) { fen.append("-"); }

        // Construct En Passant Position
        fen.append(" ");
        if (enPassantPosition == null) {
            fen.append("-");
        } else {
            fen.append(enPassantPosition.toAlgebraic());
        }

        // Construct Half Move Clock
        fen.append(" ").append(halfMoveClock);

        // Construct Full Move Number
        fen.append(" ").append(fullMoveNumber);

        // Return
        return fen.toString();
    }

    // Getters
    public Board getBoard() { return board; }
    public Color getCurrentPlayer() { return currentPlayer; }
    public CastlingRights getCastlingRights() { return castlingRights; }
    public Position getEnPassantPosition() { return enPassantPosition; }
    public int getHalfMoveClock() { return halfMoveClock; }
    public int getFullMoveNumber() { return fullMoveNumber; }
}

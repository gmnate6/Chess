package com.nathanholmberg.chess.engine.utils;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.exceptions.IllegalNotationException;
import com.nathanholmberg.chess.engine.game.Board;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.game.ChessTimer;
import com.nathanholmberg.chess.engine.pieces.Piece;
import com.nathanholmberg.chess.engine.types.CastlingRights;
import com.nathanholmberg.chess.engine.types.Position;

public class FEN {
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

    public static ChessGame getGame(String fen, ChessTimer chessTimer) {
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
        return new ChessGame(board, currentPlayer, halfMoveClock, fullMoveNumber, chessTimer);
    }

    public static String getFENBoardAndTurn(ChessGame chessGame) {
        StringBuilder fen = new StringBuilder();

        // Construct Board
        for (int rank = 7; rank >= 0; rank--) {
            int emptyCount = 0;

            // Loop through files
            for (int file = 0; file < 8; file++) {
                Position currentPosition = new Position(file, rank);
                Piece currentPiece = chessGame.board.getPieceAt(currentPosition);

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
        fen.append(" ").append(chessGame.getTurn() == Color.WHITE ? "w" : "b");
        return fen.toString();
    }

    public static String getFEN(ChessGame chessGame) {
        StringBuilder fen = new StringBuilder();

        // Construct Board
        fen.append(getFENBoardAndTurn(chessGame));

        // Construct Castling Rights
        fen.append(" ");

        CastlingRights castlingRights = chessGame.board.getCastlingRights();
        if (castlingRights.isWhiteKingSide()) { fen.append("K"); }
        if (castlingRights.isWhiteQueenSide()) { fen.append("Q"); }
        if (castlingRights.isBlackKingSide()) { fen.append("k"); }
        if (castlingRights.isBlackQueenSide()) { fen.append("q"); }

        // If no castling rights add "-"
        if (castlingRights.isNone()) { fen.append("-"); }

        // Construct En Passant Position
        Position enPassantPosition = chessGame.board.getEnPassantPosition();
        fen.append(" ");
        if (enPassantPosition == null) {
            fen.append("-");
        } else {
            fen.append(enPassantPosition.toAlgebraic());
        }

        // Construct Half Move Clock
        fen.append(" ").append(chessGame.getHalfMoveClock());

        // Construct Full Move Number
        fen.append(" ").append(chessGame.getFullMoveNumber());

        // Return
        return fen.toString();
    }
}

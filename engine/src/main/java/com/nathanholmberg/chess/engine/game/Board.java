package com.nathanholmberg.chess.engine.game;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.exceptions.IllegalMoveException;
import com.nathanholmberg.chess.engine.pieces.*;
import com.nathanholmberg.chess.engine.types.CastlingRights;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;

import java.util.ArrayList;
import java.util.List;

public class  Board {
    private final Piece[][] board = new Piece[8][8];
    private Position enPassantPosition = null;
    private CastlingRights castlingRights = new CastlingRights();

    public Board() {
        this.setup();
    }

    public static Board getEmptyBoard() {
        Board board = new Board();
        board.clear();
        return board;
    }

    public void clear() {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                board[file][rank] = null;
            }
        }
    }

    public void setup() {
        // Clear
        this.clear();

        // White Pieces
        this.setPieceAt(new Position(0, 0), new Rook(Color.WHITE));
        this.setPieceAt(new Position(1, 0), new Knight(Color.WHITE));
        this.setPieceAt(new Position(2, 0), new Bishop(Color.WHITE));
        this.setPieceAt(new Position(3, 0), new Queen(Color.WHITE));
        this.setPieceAt(new Position(4, 0), new King(Color.WHITE));
        this.setPieceAt(new Position(5, 0), new Bishop(Color.WHITE));
        this.setPieceAt(new Position(6, 0), new Knight(Color.WHITE));
        this.setPieceAt(new Position(7, 0), new Rook(Color.WHITE));
        for (int file = 0; file < 8; file++) {
            this.setPieceAt(new Position(file, 1), new Pawn(Color.WHITE));
        }

        // Black Pieces
        this.setPieceAt(new Position(0, 7), new Rook(Color.BLACK));
        this.setPieceAt(new Position(1, 7), new Knight(Color.BLACK));
        this.setPieceAt(new Position(2, 7), new Bishop(Color.BLACK));
        this.setPieceAt(new Position(3, 7), new Queen(Color.BLACK));
        this.setPieceAt(new Position(4, 7), new King(Color.BLACK));
        this.setPieceAt(new Position(5, 7), new Bishop(Color.BLACK));
        this.setPieceAt(new Position(6, 7), new Knight(Color.BLACK));
        this.setPieceAt(new Position(7, 7), new Rook(Color.BLACK));
        for (int file = 0; file < 8; file++) {
            this.setPieceAt(new Position(file, 6), new Pawn(Color.BLACK));
        }
    }

    public Board getDeepCopy() {
        Board copy = Board.getEmptyBoard();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position position = new Position(file, rank);
                Piece piece = getPieceAt(position);
                if (piece != null) {
                    copy.setPieceAt(position, piece.getDeepCopy());
                }
            }
        }
        copy.setEnPassantPosition(getEnPassantPosition());
        copy.setCastlingRights(getCastlingRights().getDeepCopy());
        return copy;
    }

    public Position getKingPosition(Color color) {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position currentPosition = new Position(file, rank);
                Piece piece = this.getPieceAt(currentPosition);
                if (piece instanceof King && piece.getColor() == color) {
                    return currentPosition;
                }
            }
        }
        throw new RuntimeException("Game state is invalid: King of color " + color + " is missing.");
    }

    public King getKing(Color color) {
        Position kingPosition = getKingPosition(color);
        return (King) getPieceAt(kingPosition);
    }

    public boolean isKingInCheck(Color color) {
        Position kingPosition = getKingPosition(color);
        return getKing(color).isChecked(kingPosition, this);
    }

    public List<Position> getPiecePositionsByColor(Color color) {
        List<Position> pieces = new ArrayList<>();

        // Loop through board
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position currentPosition = new Position(file, rank);
                Piece currentPiece = getPieceAt(currentPosition);
                if (currentPiece == null) { continue; }
                if (currentPiece.getColor() ==  color) {
                    pieces.add(currentPosition);
                }
            }
        }
        return pieces;
    }

    public List<Piece> getPiecesByColor(Color color) {
        List<Piece> pieces = new ArrayList<>();

        // Loop through board
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position currentPosition = new Position(file, rank);
                Piece currentPiece = getPieceAt(currentPosition);
                if (currentPiece == null) { continue; }
                if (currentPiece.getColor() ==  color) {
                    pieces.add(currentPiece);
                }
            }
        }
        return pieces;
    }

    public void executeMove(Move move) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        Piece pieceToMove = getPieceAt(initialPosition);

        // No Piece To Move
        if (pieceToMove == null) {
            throw new IllegalMoveException("Illegal Move: No piece at the initial position to move.");
        }

        // Execute Piece To Move Weirdness
        pieceToMove.specialMoveExecution(move, this);

        // Update Board
        pieceToMove = getPieceAt(initialPosition);
        setPieceAt(initialPosition, null);
        setPieceAt(finalPosition, pieceToMove);
    }

    // Getters
    public Piece getPieceAt(Position position) { return board[position.file()][position.rank()]; }
    public Position getEnPassantPosition() { return enPassantPosition; }
    public CastlingRights getCastlingRights() { return castlingRights; }

    // Setters
    public void setPieceAt(Position position, Piece piece) { board[position.file()][position.rank()] = piece; }
    public void setEnPassantPosition(Position enPassantPosition) { this.enPassantPosition = enPassantPosition; }
    public void setCastlingRights(CastlingRights castlingRights) { this.castlingRights = castlingRights; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                if (board[file][rank] == null) {
                    sb.append(".");
                } else {
                    Piece piece = this.getPieceAt(new Position(file, rank));
                    sb.append(piece.toString());
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

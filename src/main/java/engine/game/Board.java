package engine.game;

import engine.utils.Position;
import engine.utils.CastlingRights;

import engine.pieces.*;

import utils.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the chess board with a 8x8 grid containing pieces.
 * Handles board setup and deep copying for game state management.
 */
public class Board {
    private final Piece[][] board = new Piece[8][8];
    private Position enPassantPosition = null;
    private CastlingRights castlingRights = new CastlingRights();

    /**
     * Default constructor: initializes a standard chessboard setup.
     */
    public Board() {
        this.setup();
    }

    /**
     * Creates an empty board with no pieces.
     *
     * @return A new board instance with all squares cleared.
     */
    public static Board getEmptyBoard() {
        Board board = new Board();
        board.clear();
        return board;
    }

    /**
     * Clears all pieces from the board.
     */
    public void clear() {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                board[file][rank] = null;
            }
        }
    }

    /**
     * Sets up the board to a standard initial chess game configuration.
     * Places all pieces for both players in their initial positions.
     */
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

    /**
     * Finds the position of the King of a given color.
     *
     * @param color The color of the King to locate.
     * @return The position of the King.
     * @throws RuntimeException If no King of the specified color is found.
     */
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

    /**
     * Retrieves the King piece of a specific color.
     *
     * @param color The color of the King.
     * @return The King piece.
     */
    public King getKing(Color color) {
        Position kingPosition = getKingPosition(color);
        return (King) getPieceAt(kingPosition);
    }

    /**
     * Returns if the King of a given color is checked.
     *
     * @param color The color of the King.
     * @return true if King is in check.
     */
    public boolean isKingInCheck(Color color) {
        Position kingPosition = getKingPosition(color);
        return getKing(color).isChecked(kingPosition, this);
    }

    /**
     * Retrieves all positions occupied by pieces of a specific color.
     *
     * @param color The color to filter by.
     * @return A list of positions occupied by pieces of the given color.
     */
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

    // Getters
    public Piece getPieceAt(Position position) { return board[position.file()][position.rank()]; }
    public Position getEnPassantPosition() { return enPassantPosition; }
    public CastlingRights getCastlingRights() { return castlingRights; }

    // Setters
    public void setPieceAt(Position position, Piece piece) { board[position.file()][position.rank()] = piece; }
    public void setEnPassantPosition(Position enPassantPosition) { this.enPassantPosition = enPassantPosition; }
    public void setCastlingRights(CastlingRights castlingRights) { this.castlingRights = castlingRights; }

    /**
     * Converts the board to a multi-line string for visualization.
     * Empty squares are represented by spaces, and pieces are represented by their symbols.
     *
     * @return A string representation of the board.
     */
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

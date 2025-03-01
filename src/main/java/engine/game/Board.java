package engine.game;

import engine.types.Move;
import engine.types.Position;
import engine.types.CastlingRights;
import engine.pieces.*;
import utils.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the chessboard in an 8x8 grid containing pieces.
 * Provides functionality for managing board state, applying game rules, and performing deep copies.
 *
 * <p>Key Responsibilities:</p>
 * <ul>
 *   <li>Initializes standard or empty board configurations.</li>
 *   <li>Manages the state of pieces, castling rights, and en passant moves.</li>
 *   <li>Retrieves information about pieces, their positions, and the state of the board.</li>
 *   <li>Handles special chess rules, such as en passant, castling, and pawn promotion.</li>
 *   <li>Executes moves and updates the board accordingly while ensuring rule enforcement.</li>
 *   <li>Provides a deep copy mechanism for preserving board state independently.</li>
 * </ul>
 */
public class  Board {
    private final Piece[][] board = new Piece[8][8];
    private Position enPassantPosition = null;
    private CastlingRights castlingRights = new CastlingRights();

    /**
     * Default constructor for the `Board` class.
     * Initializes a chessboard with the standard starting position
     * for all pieces on both sides.
     */
    public Board() {
        this.setup();
    }

    /**
     * Creates and returns an empty chessboard with all squares cleared.
     * This can be used for custom game setups or testing purposes.
     *
     * @return A new `Board` instance with no pieces on the board.
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
     * Configures the board for the standard initial chess game state.
     * Places all pieces for both White and Black players in their starting positions.
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
     * Creates an independent, deep copy of the current board state.
     * Ensures all mutable attributes, such as:
     * <ul>
     *   <li>The positions and states of all pieces.</li>
     *   <li>Castling rights for both players.</li>
     *   <li>En passant state (if applicable).</li>
     * </ul>
     * are duplicated in the new instance. Changes made to the copied board
     * do not affect the original.
     *
     * @return A deep copy of the board as a new `Board` instance.
     */
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

    /**
     * Finds and returns the position of the King of the specified color.
     *
     * @param color The color of the King to locate (Color.WHITE or Color.BLACK).
     * @return The `Position` of the King on the board.
     * @throws RuntimeException If no King of the specified color is found on the board.
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
     * Retrieves the King piece of the specified color.
     *
     * @param color The color of the King to retrieve (Color.WHITE or Color.BLACK).
     * @return The `King` piece object for the given color.
     */
    public King getKing(Color color) {
        Position kingPosition = getKingPosition(color);
        return (King) getPieceAt(kingPosition);
    }

    /**
     * Checks whether the King of the specified color is currently in check.
     * A King is in check if it is under attack by any opposing pieces.
     *
     * @param color The color of the King to check (Color.WHITE or Color.BLACK).
     * @return `true` if the King is in check; otherwise, `false`.
     */
    public boolean isKingInCheck(Color color) {
        Position kingPosition = getKingPosition(color);
        return getKing(color).isChecked(kingPosition, this);
    }

    /**
     * Retrieves all positions on the board that are occupied by pieces of the specified color.
     *
     * @param color The color to filter by (Color.WHITE or Color.BLACK).
     * @return A list of `Position` objects representing the positions of the pieces.
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

    /**
     * Retrieves all pieces of the specified color currently on the board.
     *
     * @param color The color of the pieces to retrieve (Color.WHITE or Color.BLACK).
     * @return A list of `Piece` objects for the specified color.
     */
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

    /**
     * Executes a specified chess move on the board and updates the board state.
     * This method handles standard and special chess rules, including:
     * <ul>
     *   <li><b>En Passant</b>: Captures the pawn and updates the en passant position.</li>
     *   <li><b>Pawn Promotion</b>: Promotes the pawn to a specified piece (default: Queen).</li>
     *   <li><b>Castling</b>: Moves the rook along with the king and updates castling rights.</li>
     *   <li><b>Castling Rights</b>: Updates castling rights when a king or rook moves.</li>
     * </ul>
     *
     * @param move The `Move` object describing the chess move to execute.
     *             The move must already be validated as legal and safe.
     * @throws IllegalArgumentException If the move is invalid or violates chess rules.
     */
    public void executeMove(Move move) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        Piece pieceToMove = getPieceAt(initialPosition);

        // No Piece To Move
        if (pieceToMove == null) {
            throw new IllegalStateException("No piece at initial position: " + initialPosition);
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

    /**
     * Converts the board to a multi-line string for visualization.
     * Empty squares are represented by '.', and pieces are represented by their symbols.
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

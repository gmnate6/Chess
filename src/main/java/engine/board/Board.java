package engine.board;

import engine.utils.CastlingRights;
import engine.pieces.*;
import engine.utils.Move;

import utils.Color;

import java.util.ArrayList;
import java.util.List;


public class Board {
    private final Piece[][] board = new Piece[8][8];
    private Position enPassantPosition = null;
    private CastlingRights castlingRights = new CastlingRights();

    // Constructor
    public Board() {
        this.setup();
    }
    public static Board getEmptyBoard() {
        Board board = new Board();
        board.clear();
        return board;
    }

    // Clear Board
    public void clear() {
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                board[file][rank] = null;
            }
        }
    }

    // Build Default Board
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

    // Get King Position
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

    // Get King Piece
    public King getKingPiece(Color color) {
        Position kingPosition = getKingPosition(color);
        return (King) getPieceAt(kingPosition);
    }

    // Is King In Check
    public boolean isKingInCheck(Color color) {
        // Find the king's position
        Position kingPosition = this.getKingPosition(color);

        // Check if the king is under attack
        King king = (King) this.getPieceAt(kingPosition);
        return king.isChecked(kingPosition, this);
    }

    // Get Deep Copy
    public Board getDeepCopy() {
        Board copy = Board.getEmptyBoard();
        for (int file = 0; file < 8; file++) {
            for (int rank = 0; rank < 8; rank++) {
                Position position = new Position(file, rank);
                Piece piece = this.getPieceAt(position);
                if (piece != null) {
                    copy.setPieceAt(position, piece.getDeepCopy());
                }
            }
        }
        copy.setEnPassantPosition(this.getEnPassantPosition());
        copy.setCastlingRights(this.getCastlingRights().getDeepCopy());
        return copy;
    }

    // Get Pieces By Color
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

    // To String
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int rank = 7; rank >= 0; rank--) {
            for (int file = 0; file < 8; file++) {
                if (board[file][rank] == null) {
                    sb.append(" ");
                } else {
                    Piece piece = this.getPieceAt(new Position(file, rank));
                    sb.append(piece.toString());
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /// Move: Makes move without Game level knowledge
    public void move(Move move) {
        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();
        Piece pieceToMove = getPieceAt(initialPosition);

        /// No Piece To Move
        if (pieceToMove == null) {
            throw new IllegalArgumentException("No piece at initial position: " + initialPosition);
        }

        /// Validate Move Legality
        if (!pieceToMove.moveLegality(move, this)) {
            throw new IllegalArgumentException("Illegal move for '" + pieceToMove.toString() +
                    "' from " + initialPosition +
                    " to " + finalPosition);
        }

        /// Pawn Weirdness
        if (pieceToMove instanceof Pawn) {
            // If enPassant
            if (((Pawn) pieceToMove).isLegalEnPassant(move, this)) {
                // Remove En Passanted Pawn
                setPieceAt(getEnPassantPosition(), null);
            }

            // Update enPassantPosition
            if (((Pawn) pieceToMove).isLegalDouble(move, this)) {
                setEnPassantPosition(finalPosition);
            } else {
                setEnPassantPosition(null);
            }

            // Promotion
            if (finalPosition.rank() == 0 || finalPosition.rank() == 7) {
                pieceToMove = Piece.charToPiece(move.getPromotionPiece(), pieceToMove.getColor());
            }
        } else {
            setEnPassantPosition(null);
        }

        /// King Weirdness
        if (pieceToMove instanceof King) {
            // If Castle King Side
            if (((King) pieceToMove).isLegalCastle(move, this, 'K')) {
                System.out.println("king side");
                Position rookPosition = initialPosition.move(3, 0);
                Piece rookPiece = getPieceAt(rookPosition);

                // Place Rook
                setPieceAt(initialPosition.move(1, 0), rookPiece);

                // Remove Old Rook
                setPieceAt(rookPosition, null);
            }

            // If Castle Queen Side
            if (((King) pieceToMove).isLegalCastle(move, this, 'Q')) {
                System.out.println("queen side");
                Position rookPosition = initialPosition.move(-4, 0);
                Piece rookPiece = getPieceAt(rookPosition);

                // Place Rook
                setPieceAt(initialPosition.move(-1, 0), rookPiece);

                // Remove Old Rook
                setPieceAt(rookPosition, null);
            }

            // Update CastlingRights
            if (pieceToMove.getColor() == Color.WHITE) {
                castlingRights.setWhiteKingSide(false);
                castlingRights.setWhiteQueenSide(false);
            } else {
                castlingRights.setBlackKingSide(false);
                castlingRights.setBlackQueenSide(false);
            }
        }

        /// Rook Weirdness
        if (pieceToMove instanceof Rook) {
            // Update CastlingRights
            if (pieceToMove.getColor() == Color.WHITE) {
                // White
                if (initialPosition.toAlgebraic().equals("a1")) {
                    castlingRights.setWhiteQueenSide(false);
                }
                if (initialPosition.toAlgebraic().equals("h1")) {
                    castlingRights.setWhiteKingSide(false);
                }
            } else {
                // Black
                if (initialPosition.toAlgebraic().equals("a8")) {
                    castlingRights.setBlackQueenSide(false);
                }
                if (initialPosition.toAlgebraic().equals("h8")) {
                    castlingRights.setBlackKingSide(false);
                }
            }
        }

        /// Update Board
        setPieceAt(initialPosition, null);
        setPieceAt(finalPosition, pieceToMove);
    }
}

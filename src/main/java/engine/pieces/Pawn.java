package engine.pieces;

import engine.board.Board;
import engine.board.Position;
import engine.utils.Move;

import utils.Color;

public class Pawn extends Piece {
    public Pawn(Color color) { super(color); }

    // Move Legality
    public boolean moveLegality(Move move, Board board) {
        // Super
        if (!super.moveLegality(move, board)) { return false; }

        // En Passant
        if (isLegalEnPassant(move, board)) { return true; }

        // Capture
        if (isLegalCapture(move, board)) { return true; }

        // Double
        if (isLegalDouble(move, board)) { return true; }

        // Single
        if (isLegalSingle(move, board)) { return true; }

        // Is Legal Move
        return false;
    }

    // Returns true if move is a legal single
    public boolean isLegalSingle(Move move, Board board) {
        // Early Returns
        if (!super.moveLegality(move, board)) { return false; }

        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();
        int fileDirection = this.getColor() == Color.WHITE ? 1 : -1;

        // Must be same file
        if (finalPosition.file() != initialPosition.file()) { return false; }

        // Must be 1 rank away
        if (finalPosition.rank() - initialPosition.rank() != fileDirection) { return false; }

        // No Piece on Final
        if (board.getPieceAt(finalPosition) != null) { return false; }

        // Is Legal Single
        return true;
    }

    // Returns true if move is a legal double
    public boolean isLegalDouble(Move move, Board board) {
        // Early Returns
        if (!super.moveLegality(move, board)) { return false; }

        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();
        int fileDirection = this.getColor() == Color.WHITE ? 1 : -1;

        // Initial Rank must be 1 (white) or 6 (black)
        if (initialPosition.rank() != (getColor() == Color.WHITE ? 1 : 6)) { return false; }

        // Must be same file
        if (finalPosition.file() != initialPosition.file()) { return false; }

        // Must be 2 rank away
        if (finalPosition.rank() - initialPosition.rank() != 2 * fileDirection) { return false; }

        // No Piece on Final
        if (board.getPieceAt(finalPosition) != null) { return false; }

        // No Piece in front of initial
        if (board.getPieceAt(initialPosition.move(0, fileDirection)) != null) {return false; }

        // Is Legal Double
        return true;
    }

    // Returns true if move is a legal capture
    public boolean isLegalCapture(Move move, Board board) {
        // Early Returns
        if (!super.moveLegality(move, board)) { return false; }

        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();
        int fileDirection = this.getColor() == Color.WHITE ? 1 : -1;

        // Must be 1 file away
        if (Math.abs(finalPosition.file() - initialPosition.file()) != 1) { return false; }

        // Must be 1 rank away
        if (finalPosition.rank() - initialPosition.rank() != fileDirection) { return false; }

        // Must have piece to capture
        if (board.getPieceAt(finalPosition) == null) { return false; }

        // Is Legal Capture
        return true;
    }

    // Returns true if move is a legal enPassant
    public boolean isLegalEnPassant(Move move, Board board) {
        // Early Returns
        if (!super.moveLegality(move, board)) { return false; }

        // Collapse Move Obj
        Position initialPosition = move.getInitialPosition();
        Position finalPosition = move.getFinalPosition();

        // Files are 1 away
        if (Math.abs(finalPosition.file() - initialPosition.file()) != 1) {
            return false;
        }

        // Ranks are 1 away (sign depends on color)
        if (finalPosition.rank() - initialPosition.rank() != (this.getColor() == Color.WHITE ? 1 : -1)) {
            return false;
        }

        // Final has no Piece
        if (board.getPieceAt(finalPosition) != null) {
            return false;
        }

        // enPassant Position must be enPassantAble
        Position enPassantPosition = new Position(finalPosition.file(), initialPosition.rank());
        if (!enPassantPosition.equals(board.getEnPassantPosition())) {
            return false;
        }

        // Is Legal En Passant
        return true;
    }
}

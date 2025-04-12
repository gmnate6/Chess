package engine.pieces;

import engine.game.Board;
import engine.types.Position;
import engine.types.Move;
import engine.utils.PieceUtils;
import utils.Color;

public class Pawn extends Piece {
    public Pawn(Color color) { super(color); }

    @Override
    public void specialMoveExecution(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // If enPassant
        if (isLegalEnPassant(move, board)) {
            // Remove En Passanted Pawn
            Position removedPawnPos = new Position(finalPosition.file(), initialPosition.rank());
            board.setPieceAt(removedPawnPos, null);
        }

        // Update enPassantPosition
        if (isLegalDouble(move, board)) {
            Position newEnPassantPosition = initialPosition.move(0, this.getColor() == Color.WHITE ? 1 : -1);
            board.setEnPassantPosition(newEnPassantPosition);
        } else {
            board.setEnPassantPosition(null);
        }

        // Promotion
        if (isLegalPromotion(move, board)) {
            board.setPieceAt(initialPosition, PieceUtils.charToPiece(move.promotionPiece(), getColor()));
        }
    }

    public boolean isLegalPromotion(Move move, Board board) {
        Position finalPosition = move.finalPosition();

        // Must be Legal Single
        if (!isLegalSingle(move, board) && !isLegalCapture(move, board)) {
            return false;
        }

        return finalPosition.rank() == 0 || finalPosition.rank() == 7;
    }

    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        // En Passant
        if (isLegalEnPassant(move, board)) { return true; }

        // Capture
        if (isLegalCapture(move, board)) { return true; }

        // Double
        if (isLegalDouble(move, board)) { return true; }

        // Single
        return isLegalSingle(move, board);
    }

    private boolean isLegalSingle(Move move, Board board) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        int fileDirection = this.getColor() == Color.WHITE ? 1 : -1;

        // Must be same file
        if (finalPosition.file() != initialPosition.file()) { return false; }

        // Must be 1 rank away
        if (finalPosition.rank() - initialPosition.rank() != fileDirection) { return false; }

        // No Piece on Final
        return board.getPieceAt(finalPosition) == null;
    }

    public boolean isLegalDouble(Move move, Board board) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
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
        return board.getPieceAt(initialPosition.move(0, fileDirection)) == null;
    }

    private boolean isLegalCapture(Move move, Board board) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();
        int fileDirection = this.getColor() == Color.WHITE ? 1 : -1;

        // Must be 1 file away
        if (Math.abs(finalPosition.file() - initialPosition.file()) != 1) { return false; }

        // Must be 1 rank away
        if (finalPosition.rank() - initialPosition.rank() != fileDirection) { return false; }

        // Must have piece to capture
        return board.getPieceAt(finalPosition) != null;
    }

    public boolean isLegalEnPassant(Move move, Board board) {
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

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
        return finalPosition.equals(board.getEnPassantPosition());
    }
}
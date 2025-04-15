package com.nathanholmberg.chess.engine.pieces;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.Board;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;
import com.nathanholmberg.chess.engine.utils.PieceUtils;

public class Rook extends Piece {
    public Rook(Color color) { super(color); }

    @Override
    public void specialMoveExecution(Move move, Board board) {
        super.specialMoveExecution(move, board);

        // Collapse Move Obj
        Position initialPosition = move.initialPosition();

        // Update CastlingRights
        if (getColor() == Color.WHITE) {
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

    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // Must be in the same rank or file
        if (initialPosition.file() != finalPosition.file() && initialPosition.rank() != finalPosition.rank()) { return false; }

        // Check if the path is clear
        return PieceUtils.isPathClear(move, board);
    }
}

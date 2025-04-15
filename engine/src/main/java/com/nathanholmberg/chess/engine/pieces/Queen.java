package com.nathanholmberg.chess.engine.pieces;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.Board;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;
import com.nathanholmberg.chess.engine.utils.PieceUtils;

public class Queen extends Piece {
    public Queen(Color color) { super(color); }

    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // Conditions for queen's movement
        boolean isStraightMove = initialPosition.file() == finalPosition.file() ||
                initialPosition.rank() == finalPosition.rank();
        boolean isDiagonalMove = Math.abs(initialPosition.file() - finalPosition.file()) ==
                Math.abs(initialPosition.rank() - finalPosition.rank());

        // Must be a straight or diagonal move
        if (!(isStraightMove || isDiagonalMove)) { return false; }

        // Check if the path is clear
        return PieceUtils.isPathClear(move, board);
    }
}

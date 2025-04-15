package com.nathanholmberg.chess.engine.pieces;

import com.nathanholmberg.chess.engine.enums.Color;
import com.nathanholmberg.chess.engine.game.Board;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;
import com.nathanholmberg.chess.engine.utils.PieceUtils;

public class Bishop extends Piece {
    public Bishop(Color color) { super(color); }

    @Override
    public boolean isPieceSpecificMoveValid(Move move, Board board) {
        // Collapse Move Obj
        Position initialPosition = move.initialPosition();
        Position finalPosition = move.finalPosition();

        // Must be a diagonal move
        if (Math.abs(initialPosition.file() - finalPosition.file()) != Math.abs(initialPosition.rank() - finalPosition.rank())) {
            return false;
        }

        // Check if the path is clear
        return PieceUtils.isPathClear(move, board);
    }
}

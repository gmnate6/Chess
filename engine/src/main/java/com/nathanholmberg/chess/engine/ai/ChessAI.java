package com.nathanholmberg.chess.engine.ai;

import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.types.Move;

public interface ChessAI {
    Move getMove(ChessGame chessGame);
    String toString();
}

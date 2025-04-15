package com.nathanholmberg.chess.engine.ai;

import com.nathanholmberg.chess.engine.game.Game;
import com.nathanholmberg.chess.engine.types.Move;

public interface ChessAI {
    Move getMove(Game game);
    String toString();
}

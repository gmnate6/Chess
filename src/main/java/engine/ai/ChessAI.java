package engine.ai;

import engine.game.Game;
import engine.types.Move;

public interface ChessAI {
    String getName();
    Move getMove(Game game);
}

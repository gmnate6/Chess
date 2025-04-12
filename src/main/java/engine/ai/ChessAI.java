package engine.ai;

import engine.game.Game;
import engine.types.Move;

public interface ChessAI {
    Move getMove(Game game);
    String toString();
}

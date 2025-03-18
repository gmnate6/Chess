package frontend.interfaces;

import engine.types.Move;
import engine.types.Position;

import java.util.List;

public interface BoardMoveListener {
    public List<Position> getLegalMoves(Position position);
    public void processPlayerMove(Move move);
}

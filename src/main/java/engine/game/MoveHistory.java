package engine.game;

import engine.types.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveHistory {
    private List<Move> moves;
    private int currentMoveIndex;

    public MoveHistory() {
        this.moves = new ArrayList<>();
        this.currentMoveIndex = -1;
    }

    // Getters
    public List<Move> getMoves() { return new ArrayList<>(moves); }
    public int getCurrentMoveIndex() { return currentMoveIndex; }
    public int getSize() { return moves.size(); }
    public Move getLastMove() {
        if (currentMoveIndex < 0) { return null; }
        return moves.get(currentMoveIndex);
    }

    public void addMove(Move move) {
        // If we undid moves and then make a new move, delete "future" moves
        if (currentMoveIndex < moves.size() - 1) {
            moves = moves.subList(0, currentMoveIndex + 1);
        }
        moves.add(move);
        currentMoveIndex++;
    }

    public Move undoMove() {
        if (currentMoveIndex >= 0) {
            Move lastMove = moves.get(currentMoveIndex);
            currentMoveIndex--;
            return lastMove;
        }
        return null;  // No moves to undo
    }

    public Move redoMove() {
        if (currentMoveIndex < moves.size() - 1) {
            currentMoveIndex++;
            return moves.get(currentMoveIndex);
        }
        return null;  // No moves to redo
    }
}

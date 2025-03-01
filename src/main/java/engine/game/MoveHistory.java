package engine.game;

import engine.types.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Tracks the history of moves in a game and provides functionality
 * to navigate through the move history (undo, redo).
 *
 * <p>The move history maintains a list of moves and an index representing
 * the current position in the history. It supports adding new moves, undoing
 * the latest move, and redoing previously undone moves.</p>
 */
public class MoveHistory {
    private List<Move> moves;
    private int currentMoveIndex;

    /**
     * Creates an empty move history with no moves recorded.
     * The current move index is initialized to -1, representing
     * the absence of moves.
     */
    public MoveHistory() {
        this.moves = new ArrayList<>();
        this.currentMoveIndex = -1;
    }

    // Getters
    public List<Move> getMoves() { return new ArrayList<>(moves); }
    public int getCurrentMoveIndex() { return currentMoveIndex; }
    public int getSize() { return moves.size(); }

    /**
     * Retrieves the last move played from the move history.
     *
     * @return The last move played, or {@code null} if no moves exist.
     */
    public Move getLastMove() {
        if (currentMoveIndex < 0) { return null; }
        return moves.get(currentMoveIndex);
    }

    /**
     * Adds a new move to the move history. If moves were undone previously,
     * all future moves will be removed before adding the new move.
     *
     * @param move The move to add to the history.
     */
    public void addMove(Move move) {
        // If we undid moves and then make a new move, delete "future" moves
        if (currentMoveIndex < moves.size() - 1) {
            moves = moves.subList(0, currentMoveIndex + 1);
        }
        moves.add(move);
        currentMoveIndex++;
    }

    /**
     * Undoes the last move in the history.
     *
     * @return The undone move, or {@code null} if no moves are available to undo.
     */
    public Move undoMove() {
        if (currentMoveIndex >= 0) {
            Move lastMove = moves.get(currentMoveIndex);
            currentMoveIndex--;
            return lastMove;
        }
        return null;  // No moves to undo
    }

    /**
     * Redoes the next move in the history, if available.
     *
     * @return The redone move, or {@code null} if no moves are available to redo.
     */
    public Move redoMove() {
        if (currentMoveIndex < moves.size() - 1) {
            currentMoveIndex++;
            return moves.get(currentMoveIndex);
        }
        return null;  // No moves to redo
    }
}

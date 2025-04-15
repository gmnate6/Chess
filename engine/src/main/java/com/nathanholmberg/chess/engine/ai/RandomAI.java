package com.nathanholmberg.chess.engine.ai;

import com.nathanholmberg.chess.engine.game.Game;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;
import com.nathanholmberg.chess.engine.utils.MoveUtils;

import java.util.List;
import java.util.Random;

public class RandomAI implements ChessAI{
    public Move getMove(Game game) {
        // Random instance
        Random random = new Random();

        // Get Initial Positions for Current Player
        List<Position> initialPositionList = game.board.getPiecePositionsByColor(game.getTurn());

        while (!initialPositionList.isEmpty()) {
            // Select Random Initial Position
            int randomInitialIndex = random.nextInt(initialPositionList.size());
            Position initialPosition = initialPositionList.get(randomInitialIndex);

            // Get Legal Moves for Initial Position
            List<Position> finalPositionList = game.getLegalMoves(initialPosition);

            // If there are legal moves, pick one and return
            if (!finalPositionList.isEmpty()) {
                Position finalPosition = finalPositionList.get(random.nextInt(finalPositionList.size()));
                Move move = new Move(initialPosition, finalPosition, '\0');
                if (MoveUtils.causesPromotion(move, game)) {
                    move = new Move(initialPosition, finalPosition, 'Q');
                }
                return move;
            }

            // Remove position from list if no legal moves are found
            initialPositionList.remove(randomInitialIndex);
        }

        // No moves available
        throw new IllegalStateException("Error: No Moves Available");
    }

    public String toString() {
        return "RandomAI";
    }
}

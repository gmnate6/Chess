package utils;

import engine.game.Game;
import engine.utils.Move;
import engine.utils.Position;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMoveAI {
    public static Move getMove(Game game) {
        // Random instance
        Random random = new Random();

        // Get All Initial Positions
        List<Position> initialPositionList = game.getBoard().getPiecePositionsByColor(game.getCurrentPlayer());

        // Get All Moves
        List<Move> allMoves = new ArrayList<>();
        for (Position initialPosition : initialPositionList) {
            List<Position> finalPositionList = game.getLegalMoves(initialPosition);
            for (Position finalPosition : finalPositionList) {
                allMoves.add(new Move(initialPosition, finalPosition, 'q'));
            }
        }
        if (allMoves.isEmpty()) {
            throw new RuntimeException("Error: No Moves Available");
        }
        int randomIndex = random.nextInt(allMoves.size());

        // Get Move
        return allMoves.get(randomIndex);
    }
}

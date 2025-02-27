package engine.utils;

import engine.game.Game;
import engine.types.Move;
import utils.Color;

import java.util.List;

public class PGN {
    public static String getPGN(Game game) {
        StringBuilder pgn = new StringBuilder();

        // Dummy Game
        Game dummyGame = new Game(null);

        // Loop Through Moves History
        List<Move> moves = game.getMoveHistory().getMoves();
        for (Move move : moves) {
            // Add Move Count
            if (dummyGame.getTurn() == Color.WHITE) {
                pgn.append(dummyGame.getFullMoveNumber()).append(". ");
            }

            // Move should be legal
            assert (dummyGame.isMoveLegal(move));

            // Add Algebraic Notation
            pgn.append(MoveUtils.toAlgebraic(move, dummyGame));
            pgn.append(" ");

            // Make Move
            dummyGame.move(move);
        }

        // Add Result
        if (!game.getGameResult().isOnGoing()){
            pgn.append(game.getGameResult().toString());
        }

        // Return PGN
        return pgn.toString().trim();
    }
}

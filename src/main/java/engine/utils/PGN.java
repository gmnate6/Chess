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
        if (!game.getResult().isOnGoing()){
            pgn.append(game.getResult().getScore());
        }

        // Return PGN
        return pgn.toString().trim();
    }

    public static Game getGame(String pgn) {
        Game game = new Game(null);

        // Split PGN into moves, excluding metadata or headers
        String[] movesArray = pgn.replaceAll("\\n", " ").split("\\s+");
        for (String moveNotation : movesArray) {
            if (moveNotation.isEmpty()) { continue; }
            if (moveNotation.matches("^\\d.*")) { continue; } // Skip turn numbers and results

            Move move = MoveUtils.fromAlgebraic(moveNotation, game);
            game.move(move);
        }
        return game;
    }
}

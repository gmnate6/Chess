package engine.utils;

import engine.exceptions.IllegalNotationException;
import engine.game.Game;
import engine.game.ChessTimer;
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

    public static Game getGame(String pgn, ChessTimer chessTimer) {
        Game game = new Game(chessTimer);

        // Early Throw
        if (pgn == null || pgn.isEmpty()) {
            throw new IllegalNotationException("Illegal PGN Notation: '" + pgn + "'");
        }

        // Split PGN into moves, excluding metadata or headers
        String gameResult = "";
        String[] movesArray = pgn.replaceAll("\\n", " ").split("\\s+");
        for (String moveNotation : movesArray) {
            if (moveNotation.isEmpty()) { continue; }
            // Skip turn numbers and results
            if (moveNotation.matches("^\\d.*")) {
                gameResult = moveNotation.trim();
                continue;
            }

            try {
                Move move = MoveUtils.fromAlgebraic(moveNotation, game);
                game.move(move);
            } catch (Exception e) {
                throw new IllegalNotationException(
                        "Illegal PGN Notation: '" + pgn + "'" +
                        "\nException occurred on move: " + game.getFullMoveNumber() + ". " + moveNotation +
                        "\n" + e.getMessage());
            }
        }

        // Check Game Result
        boolean notationSaysGameOver = (
                gameResult.equals("1-0") ||
                gameResult.equals("0-1") ||
                gameResult.equals("1/2-1/2")
        );

        // If notation says it's over, it's over
        if (notationSaysGameOver && game.inPlay()) {
            if (gameResult.equals("1-0")) {
                game.resign(Color.BLACK);
            } else if (gameResult.equals("0-1")) {
                game.resign(Color.WHITE);
            } else {
                game.drawAgreement();
            }
            game.removeTimer();
        }

        // If game says it's over, but notation does not
        if (!notationSaysGameOver && !game.inPlay()) {
            throw new IllegalNotationException("Illegal PGN Notation: '" + pgn + "'. Game result does not match notation.");
        }

        // Return
        return game;
    }
}

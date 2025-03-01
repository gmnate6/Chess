package engine.utils;

import engine.game.Game;
import engine.types.Move;
import utils.Color;

import java.util.List;

/**
 * <p><strong>Class Purpose:</strong></p>
 * <p>The <code>PGN</code> class is a utility for working with Portable Game Notation (PGN), the standard
 * format for representing chess games. It provides methods to convert a <code>Game</code> object to
 * PGN notation and vice versa, enabling the storage, sharing, and reconstruction of chess games in a
 * universal format.</p>
 *
 * <p><strong>Key Features:</strong></p>
 * <ul>
 *   <li>Serialization of a <code>Game</code> object into PGN notation.</li>
 *   <li>Deserialization of a PGN notation string back into a <code>Game</code> object.</li>
 *   <li>Maintains adherence to proper chess rules and algebraic notation during conversions.</li>
 * </ul>
 */
public class PGN {
    /**
     * Converts a chess <code>Game</code> object into its corresponding PGN string representation.
     * This includes turn-by-turn move records in algebraic notation and the game's result.
     *
     * @param game The <code>Game</code> object to be converted into PGN notation.
     * @return A <code>String</code> containing the PGN representation of the <code>Game</code>.
     */
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

    /**
     * Parses a PGN notation string and reconstructs the corresponding <code>Game</code> object by simulating its moves.
     *
     * @param pgn The PGN string to be parsed into a <code>Game</code>.
     * @return A <code>Game</code> object reconstructed from the provided PGN.
     */
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

package engine;

import engine.ai.RandomAI;
import engine.game.Game;
import engine.utils.FEN;
import engine.utils.PGN;

import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This test verifies the consistency and accuracy of the PGN (Portable Game Notation) generator and parser in the game engine.
 * It runs a random game simulation using the `RandomAI` and ensures that:
 * 1. A PGN string correctly represents the game played.
 * 2. A game reconstructed from the PGN string reproduces the exact end state of the original game.
 *
 * <p>To achieve this, it compares the FEN (Forsyth-Edwards Notation) strings of the original game
 * and the game reconstructed from the PGN, asserting they are identical.
 * The test is repeated multiple times to ensure reliability under various random game scenarios.</p>
 */
public class PGNTest {
    @RepeatedTest(20)
    public void randomGameTest() {
        Game game = new Game(null);

        // Play random game
        while (game.inPlay()) {
            game.move(RandomAI.getMove(game));
        }

        // Get pgn
        String pgn = PGN.getPGN(game);
        Game gameFromPGN = PGN.getGame(pgn);

        // Check if pgn reconstructed game using FEN
        assertEquals(FEN.getFEN(game), FEN.getFEN(gameFromPGN));
    }
}

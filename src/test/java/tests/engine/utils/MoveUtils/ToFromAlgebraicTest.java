package tests.engine.utils.MoveUtils;

import engine.ai.RandomAI;
import engine.game.Game;
import engine.types.Move;
import engine.utils.FEN;
import engine.utils.MoveUtils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;

/**
 * This test ensures the correctness of the conversion between `Move` objects and their algebraic notation representation
 * during a random game simulation.

 * <p>It verifies that:
 * 1. A move can be accurately converted to algebraic notation and back to the original `Move` object.
 * 2. The reconstructed `Move` is identical to the original `Move` and correctly applicable within the game's context.</p>
 *
 * The test also confirms that the game's FEN (Forsyth-Edwards Notation) remains valid after reconstructing and applying each move.
 * It is repeated multiple times to ensure robustness across various random game scenarios.
 */
public class ToFromAlgebraicTest {
    @RepeatedTest(20)
    public void randomGameTest() {
        Game game = new Game(null);

        // Play random game
        while (game.inPlay()) {
            Move originalMove = RandomAI.getMove(game);

            // To and From
            String algebraic = MoveUtils.toAlgebraic(originalMove, game);
            Move move = MoveUtils.fromAlgebraic(algebraic, game);

            assertEquals(originalMove, move, FEN.getFEN(game));

            // Do move
            game.move(move);
        }
    }
}

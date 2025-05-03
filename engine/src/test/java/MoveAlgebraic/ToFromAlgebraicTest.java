package MoveAlgebraic;

import com.nathanholmberg.chess.engine.ai.RandomAI;
import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.utils.FEN;
import com.nathanholmberg.chess.engine.utils.MoveUtils;

import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @RepeatedTest(5)
    public void randomGameTest() {
        ChessGame chessGame = new ChessGame();

        // Play random game
        while (chessGame.inPlay()) {
            Move originalMove = new RandomAI().getMove(chessGame);

            // To and From
            String algebraic = MoveUtils.toAlgebraic(originalMove, chessGame);
            Move move = MoveUtils.fromAlgebraic(algebraic, chessGame);

            assertEquals(originalMove, move, FEN.getFEN(chessGame));

            // Do move
            chessGame.move(move);
        }
    }
}

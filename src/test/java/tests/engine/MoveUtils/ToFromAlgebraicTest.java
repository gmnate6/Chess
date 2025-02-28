package tests.engine.MoveUtils;

import engine.ai.RandomAI;
import engine.game.Game;
import engine.types.Move;
import engine.utils.FEN;
import engine.utils.MoveUtils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.RepeatedTest;

public class ToFromAlgebraicTest {
    @RepeatedTest(1_000)
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

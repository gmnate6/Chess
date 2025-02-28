package tests.engine;

import engine.ai.RandomAI;
import engine.game.Game;
import engine.utils.FEN;
import engine.utils.PGN;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

public class PGNTest {
    @RepeatedTest(1_000)
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
        Assertions.assertEquals(FEN.getFEN(game), FEN.getFEN(gameFromPGN));
    }
}

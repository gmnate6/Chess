package engine;

import engine.ai.RandomAI;
import engine.exceptions.IllegalNotationException;
import engine.game.Game;
import engine.types.Move;
import engine.utils.FEN;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Unit tests for validating the functionality of the FEN (Forsyth-Edwards Notation) utility.
 *
 * <p>This class contains test cases to ensure correct FEN string generation,
 * parsing, and validation, as well as handling of edge cases and errors.
 * It uses JUnit to perform assertions on various FEN-related operations.</p>
 *
 * <p>Key functionalities tested include:</p>
 * <ul>
 *   <li>Verification of FEN string consistency during random games.</li>
 *   <li>Correct creation of game objects from valid FEN strings.</li>
 *   <li>Detection and handling of illegal or malformed FEN strings.</li>
 *   <li>Accuracy of FEN generation in standard chess positions.</li>
 * </ul>
 *
 * <p>These tests ensure the robustness of the FEN utility, which
 * is crucial for serializing and deserializing chess game states.</p>
 */
public class FENTest {
    @RepeatedTest(5)
    public void randomGameTest() {
        Game game = new Game(null);

        // Play random game
        while (game.inPlay()) {
            Move move = RandomAI.getMove(game);

            // To and From
            String expectedFEN = FEN.getFEN(game);
            String actualFEN = FEN.getFEN(FEN.getGame(expectedFEN, null));

            assertEquals(expectedFEN, actualFEN);

            // Do move
            game.move(move);
        }
    }

    @Test
    public void getFENTest() {
        Game game = new Game(null);
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", FEN.getFEN(game));
    }

    @Test
    public void getGameTest() {
        FEN.getGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", null);
    }

    @Test
    public void illegalFENTest() {
        // Empty
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("", null));
        // Null
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame(null, null));
        // Missing king
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("8/8/2k5/8/8/8/8/8 w KQkq - 0 1", null));
        // King left in check
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("8/8/2K5/8/8/8/2k2R2/8 w KQkq - 0 1", null));
        // Missing field
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq 0 2", null));
        // Empty string field
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR  KQkq 0 2", null));
        // Extra blank square
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("rnbqkbnr/pppppppp/9/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 2", null));
        // Extra piece (pawn)
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("rnbqkbnr/ppppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 2", null));
        // Pawn left on first or eight rank
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("pnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 2", null));
        // One less piece (pawn)
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPP/RNBQKBNR w KQkq - 0 2", null));
        // Current turn not 'w' nor 'b'
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR f KQkq - 0 2", null));
        // Negative half move counter
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - -1 2", null));
        // Negative full move number
        assertThrows(IllegalNotationException.class, () ->
                FEN.getGame("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 -5", null));
    }
}

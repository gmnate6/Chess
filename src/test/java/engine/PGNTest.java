package engine;

import engine.ai.RandomAI;
import engine.exceptions.IllegalNotationException;
import engine.game.Game;
import engine.utils.PGN;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * Unit tests for validating the functionality of the PGN (Portable Game Notation) utility.
 *
 * <p>This class contains test cases to ensure correct PGN string generation,
 * parsing, and validation, as well as handling of edge cases and errors.
 * It uses JUnit to perform assertions on various PGN-related operations.</p>
 *
 * <p>Key functionalities tested include:</p>
 * <ul>
 *   <li>Verification of PGN consistency during random games.</li>
 *   <li>Correct creation of game objects from valid PGN strings.</li>
 *   <li>Detection and handling of illegal or malformed PGN strings.</li>
 *   <li>Accuracy of PGN generation for chess games with different scenarios.</li>
 * </ul>
 *
 * <p>These tests ensure the reliability of the PGN utility, which is essential
 * for encoding and decoding game records in a format widely used in the chess community.</p>
 */
public class PGNTest {
    @RepeatedTest(5)
    public void randomGameTest() {
        Game game = new Game(null);

        // Play random game
        while (game.inPlay()) {
            game.move(RandomAI.getMove(game));
        }

        // Get pgn
        String pgn = PGN.getPGN(game);
        Game gameFromPGN = PGN.getGame(pgn, null);

        // Check if pgn reconstructed game using FEN
        assertEquals(PGN.getPGN(game), PGN.getPGN(gameFromPGN));
    }

    @Test
    public void getGameTest() {
        PGN.getGame("1. d4 d5 2. c4 Bf5 3. Nc3 Nf6 4. Nf3 e6 5. Bg5 Be7 6. e3 c6 7. Bd3 Bg4 8. O-O\n" +
                "O-O 9. Re1 h6 10. Bh4 g5 11. Bg3 Bb4 12. a3 Ba5 13. b4 Bc7 14. Bxc7 Qxc7 15. c5\n" +
                "a5 16. h3 Bh5 17. Qe2 axb4 18. axb4 Rxa1 19. Rxa1 Nbd7 20. b5 cxb5 21. Nxb5 Qc6\n" +
                "22. Ra7 Ne4 23. g4 Bg6 24. Qb2 Rb8 25. Nc3 Ndf6 26. Bb5 Qc7 27. Nxe4 Nxe4 28.\n" +
                "Ne5 Qc8 29. Qb1 Ng3 30. Nxg6 Ne2+ 31. Kg2 fxg6 32. Qxg6+ Kh8 33. Qxh6+ Kg8 34.\n" +
                "Qxg5+ Kf7 35. Qh5+ Ke7 36. Bxe2 Qc6 37. Qg5+ Kd7 38. h4 Rd8 39. h5 Qc7 40. h6\n" +
                "Qb8 41. Ra1 1-0", null);
    }

    @Test
    public void illegalPGNTest() {
        // Empty
        assertThrows(IllegalNotationException.class, () ->
                PGN.getGame("", null));
        // Null
        assertThrows(IllegalNotationException.class, () ->
                PGN.getGame(null, null));
        // Missing result and moves cause checkmate
        assertThrows(IllegalNotationException.class, () ->
                PGN.getGame("1. d4 c6 2. Bf4 d5 3. e3 Nf6 4. Nf3 Bg4 5. Be2 e6 6. Ne5 Bxe2 7. Qxe2 Nbd7 8. c3\n" +
                        "Bd6 9. Nd2 O-O 10. O-O-O Rc8 11. Rdg1 c5 12. g4 Qc7 13. Ndf3 h6 14. g5 hxg5 15.\n" +
                        "Bxg5 cxd4 16. exd4 Rfe8 17. h4 Ne4 18. Qe1 Bxe5 19. Nxe5 Nxe5 20. dxe5 Qxe5 21.\n" +
                        "f4 Qf5 22. h5 Nxg5 23. fxg5 g6 24. hxg6 fxg6 25. Rh6 Kg7 26. Qf1 Rf8 27. Qxf5\n" +
                        "Rxf5 28. Rg2 e5 29. Kc2 e4 30. Kd2 Re8 31. Ke3 Rf3+ 32. Kd4 e3 33. Re2 Re4+ 34.\n" +
                        "Kxd5 Re7 35. Kd4 b5 36. b4 Rg3 37. Rh1 Rg4+ 38. Kd3 Rg3 39. Rhe1 Rd7+ 40. Ke4\n" +
                        "Re7+ 41. Kf4 Rh3 42. Kg4 Rh5 43. Rxe3 Rf7 44. Re7 Rxe7 45. Rxe7+ Kf8 46. Re6 Kf7\n" +
                        "47. Rf6+ Kg7 48. Ra6 Rh2 49. Rxa7+ Kf8 50. Ra5 Kf7 51. Rxb5 Rxa2 52. Rb7+ Ke6\n" +
                        "53. Rb6+ Kf7 54. Rf6+ Kg7 55. Kf4 Rf2+ 56. Ke5 Re2+ 57. Kd6 Rd2+ 58. Kc6 Ra2 59.\n" +
                        "b5 Ra5 60. b6 Ra6 61. Kc7 Kh7 62. b7 Rxf6 63. gxf6 g5 64. b8=Q g4 65. Kd7 g3 66.\n" +
                        "Qxg3 Kh6 67. f7 Kh7 68. f8=R Kh6 69. Rg8 Kh5 70. Rg5+ Kh6 71. Rg8 Kh7 72. Qg7#", null));
    }
}

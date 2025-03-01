package tests.engine.utils.MoveUtils;

import engine.game.Game;
import engine.pieces.Pawn;
import engine.pieces.Piece;
import engine.types.Move;
import engine.types.Position;
import engine.utils.FEN;
import engine.utils.MoveUtils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This class contains a suite of unit tests to validate the correctness of the
 * `MoveUtils.fromAlgebraic` method, which parses algebraic notation and converts it back into
 * corresponding `Move` objects.
 *
 * <p>The tests cover various chess scenarios such as pawn moves, promotions, castling, captures,
 * and edge cases involving checks, checkmates, and ambiguity resolution. By testing these scenarios,
 * the suite ensures the reliability of the algebraic notation parsing logic and its adherence to chess rules.</p>
 */
public class FromAlgebraicTest {
    /**
     * Verifies the correct parsing of pawn moves from algebraic notation.
     */
    @Test
    public void testPawn() {
        // White
        Game game = FEN.getGame("8/4kpp1/8/8/8/8/1PPK4/8 w - - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("b2"), Position.fromAlgebraic("b3"), '\0');
        Move move2 = new Move(Position.fromAlgebraic("c2"), Position.fromAlgebraic("c4"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("b3", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("c4", game));

        // Black
        game = FEN.getGame("8/4kpp1/8/8/8/8/1PPK4/8 b - - 0 1", null);
        move1 = new Move(Position.fromAlgebraic("g7"), Position.fromAlgebraic("g6"), '\0');
        move2 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f5"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("g6", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("f5", game));

        // En Passant
        game = FEN.getGame("r3kb1r/pp2p1pp/3qb3/1PpPP3/4n3/1n1PBPPB/P1Q5/RN2K1NR w KQkq c6 0 16", null);
        move1 = new Move(Position.fromAlgebraic("b5"), Position.fromAlgebraic("c6"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("bxc6", game));
    }

    /**
     * Ensures that pawn promotions are parsed correctly from algebraic notation.
     */
    @Test
    public void testPromotion() {
        // White
        Game game = FEN.getGame("8/5P2/k7/8/8/K7/5p2/8 w - - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'Q');
        Move move2 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'R');
        Move move3 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'N');
        Move move4 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'B');
        assertEquals(move1, MoveUtils.fromAlgebraic("f8=Q", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("f8=R", game));
        assertEquals(move3, MoveUtils.fromAlgebraic("f8=N", game));
        assertEquals(move4, MoveUtils.fromAlgebraic("f8=B", game));

        // Black
        game = FEN.getGame("8/5P2/k7/8/8/K7/5p2/8 b - - 0 1", null);
        move1 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'Q');
        move2 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'R');
        move3 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'N');
        move4 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'B');
        assertEquals(move1, MoveUtils.fromAlgebraic("f1=Q", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("f1=R", game));
        assertEquals(move3, MoveUtils.fromAlgebraic("f1=N", game));
        assertEquals(move4, MoveUtils.fromAlgebraic("f1=B", game));
    }

    /**
     * Checks the accurate parsing of castling moves (kingside and queenside) from algebraic notation.
     */
    @Test
    public void testCastling() {
        // White
        Game game = FEN.getGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("e1"), Position.fromAlgebraic("g1"), '\0');
        Move move2 = new Move(Position.fromAlgebraic("e1"), Position.fromAlgebraic("c1"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("O-O", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("O-O-O", game));

        // Black
        game = FEN.getGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R b KQkq - 0 1", null);
        move1 = new Move(Position.fromAlgebraic("e8"), Position.fromAlgebraic("g8"), '\0');
        move2 = new Move(Position.fromAlgebraic("e8"), Position.fromAlgebraic("c8"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("O-O", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("O-O-O", game));
    }

    /**
     * Tests the resolution of ambiguity for algebraic notation when multiple pieces of the same type can move.
     */
    @Test
    public void testAmbiguity() {
        // No Ambiguity
        Game game = FEN.getGame("8/8/4n3/8/8/5r2/8/k1K5 b - - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("e6"), Position.fromAlgebraic("f4"), '\0');
        Move move2 = new Move(Position.fromAlgebraic("f3"), Position.fromAlgebraic("f4"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("Nf4", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("Rf4", game));

        // Similar File
        game = FEN.getGame("8/4R3/8/8/8/4R3/8/k1K5 w - - 1 1", null);
        move1 = new Move(Position.fromAlgebraic("e3"), Position.fromAlgebraic("e5"), '\0');
        move2 = new Move(Position.fromAlgebraic("e7"), Position.fromAlgebraic("e5"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("R3e5", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("R7e5", game));

        // Similar Rank
        game = FEN.getGame("8/8/8/2R3R1/8/8/8/k1K5 w - - 2 1", null);
        move1 = new Move(Position.fromAlgebraic("c5"), Position.fromAlgebraic("e5"), '\0');
        move2 = new Move(Position.fromAlgebraic("g5"), Position.fromAlgebraic("e5"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("Rce5", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("Rge5", game));

        // Both
        game = FEN.getGame("8/8/4Q1Q1/8/4Q1Q1/8/8/k1K5 w - - 3 1", null);
        move1 = new Move(Position.fromAlgebraic("e4"), Position.fromAlgebraic("f5"), '\0');
        move2 = new Move(Position.fromAlgebraic("e6"), Position.fromAlgebraic("f5"), '\0');
        Move move3 = new Move(Position.fromAlgebraic("g4"), Position.fromAlgebraic("f5"), '\0');
        Move move4 = new Move(Position.fromAlgebraic("g6"), Position.fromAlgebraic("f5"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("Qe4f5", game));
        assertEquals(move2, MoveUtils.fromAlgebraic("Qe6f5", game));
        assertEquals(move3, MoveUtils.fromAlgebraic("Qg4f5", game));
        assertEquals(move4, MoveUtils.fromAlgebraic("Qg6f5", game));
    }

    /**
     * Ensures that capturing moves are correctly parsed from algebraic notation.
     */
    @Test
    public void testCapture() {
        // White
        Game game = FEN.getGame("8/8/8/4p1R1/8/4r1P1/8/k1K5 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("g5"), Position.fromAlgebraic("e5"), '\0');
        assertEquals(move, MoveUtils.fromAlgebraic("Rxe5", game));

        // Black
        game = FEN.getGame("8/8/8/4p1R1/8/4r1P1/8/k1K5 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("e3"), Position.fromAlgebraic("g3"), '\0');
        assertEquals(move, MoveUtils.fromAlgebraic("Rxg3", game));
    }

    /**
     * Verifies that moves resulting in a check to the opposing king are correctly parsed from algebraic notation.
     */
    @Test
    public void testChecks() {
        // White
        Game game = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("b5"), Position.fromAlgebraic("a5"), '\0');
        assertEquals(move, MoveUtils.fromAlgebraic("Ra5+", game));

        // Black
        game = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("h2"), Position.fromAlgebraic("h1"), '\0');
        assertEquals(move, MoveUtils.fromAlgebraic("Rh1+", game));
    }

    /**
     * Checks that moves resulting in a checkmate are correctly parsed from algebraic notation.
     */
    @Test
    public void testCheckmates() {
        // White
        Game game = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("c6"), Position.fromAlgebraic("a6"), '\0');
        assertEquals(move, MoveUtils.fromAlgebraic("Ra6#", game));

        // Black
        game = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("g3"), Position.fromAlgebraic("g1"), '\0');
        assertEquals(move, MoveUtils.fromAlgebraic("Rg1#", game));
    }

    /**
     * Tests the parsing of a complex move involving promotion, capturing, and checkmate from algebraic notation.
     */
    @Test
    public void testPromotion_Capture_Checkmate() {
        // White
        Game game = FEN.getGame("2q2k2/1P5R/8/8/8/8/1p5r/2Q2K2 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("b7"), Position.fromAlgebraic("c8"), 'R');
        assertEquals(move, MoveUtils.fromAlgebraic("bxc8=R#", game));

        // Black
        game = FEN.getGame("2q2k2/1P5R/8/8/8/8/1p5r/2Q2K2 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("b2"), Position.fromAlgebraic("c1"), 'R');
        assertEquals(move, MoveUtils.fromAlgebraic("bxc1=R#", game));
    }

    /**
     * Verifies the correct parsing of castling moves that result in checkmate from algebraic notation.
     */
    @Test
    public void testCastling_Checkmate() {
        Game game = FEN.getGame("8/8/8/8/8/8/7R/k3K2R w K - 0 1", null);
        Move move = new Move(Position.fromAlgebraic("e1"), Position.fromAlgebraic("g1"), '\0');
        assertEquals(move, MoveUtils.fromAlgebraic("O-O#", game));
    }
}

package engine.MoveAlgebraic;

import engine.game.Game;
import engine.types.Move;
import engine.types.Position;
import engine.utils.FEN;
import engine.utils.MoveUtils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class contains a collection of unit tests for verifying the correctness of
 * the `MoveUtils.toAlgebraic` method, which converts moves to their algebraic notation representation.
 *
 * <p>Each test ensures that specific chess scenarios (such as pawn moves, promotions, castling, etc.)
 * are correctly translated to algebraic notation according to the rules and conventions of chess notation.
 * By covering a variety of common and edge cases in chess, this suite aims to validate the accuracy
 * and reliability of the move-to-algebraic conversion logic.</p>
 */
public class ToAlgebraicTest {
    /**
     * Verifies that pawn moves are correctly translated to algebraic notation.
     */
    @Test
    public void testPawn() {
        // White
        Game game = FEN.getGame("8/4kpp1/8/8/8/8/1PPK4/8 w - - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("b2"), Position.fromAlgebraic("b3"), '\0');
        Move move2 = new Move(Position.fromAlgebraic("c2"), Position.fromAlgebraic("c4"), '\0');
        assertEquals("b3", MoveUtils.toAlgebraic(move1, game));
        assertEquals("c4", MoveUtils.toAlgebraic(move2, game));

        // Black
        game = FEN.getGame("8/4kpp1/8/8/8/8/1PPK4/8 b - - 0 1", null);
        move1 = new Move(Position.fromAlgebraic("g7"), Position.fromAlgebraic("g6"), '\0');
        move2 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f5"), '\0');
        assertEquals("g6", MoveUtils.toAlgebraic(move1, game));
        assertEquals("f5", MoveUtils.toAlgebraic(move2, game));

        // En Passant
        game = FEN.getGame("r3kb1r/pp2p1pp/3qb3/1PpPP3/4n3/1n1PBPPB/P1Q5/RN2K1NR w KQkq c6 0 16", null);
        move1 = new Move(Position.fromAlgebraic("b5"), Position.fromAlgebraic("c6"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("bxc6", game));
        assertEquals("bxc6", MoveUtils.toAlgebraic(move1, game));
    }

    /**
     * Ensures that pawn promotions are correctly represented in algebraic notation.
     */
    @Test
    public void testPromotion() {
        // White
        Game game = FEN.getGame("8/5P2/k7/8/8/K7/5p2/8 w - - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'Q');
        Move move2 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'R');
        Move move3 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'N');
        Move move4 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'B');
        assertEquals("f8=Q", MoveUtils.toAlgebraic(move1, game));
        assertEquals("f8=R", MoveUtils.toAlgebraic(move2, game));
        assertEquals("f8=N", MoveUtils.toAlgebraic(move3, game));
        assertEquals("f8=B", MoveUtils.toAlgebraic(move4, game));

        // Black
        game = FEN.getGame("8/5P2/k7/8/8/K7/5p2/8 b - - 0 1", null);
        move1 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'Q');
        move2 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'R');
        move3 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'N');
        move4 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'B');
        assertEquals("f1=Q", MoveUtils.toAlgebraic(move1, game));
        assertEquals("f1=R", MoveUtils.toAlgebraic(move2, game));
        assertEquals("f1=N", MoveUtils.toAlgebraic(move3, game));
        assertEquals("f1=B", MoveUtils.toAlgebraic(move4, game));
    }

    /**
     * Checks that castling moves (king side and queen side) are correctly represented in algebraic notation.
     */
    @Test
    public void testCastling() {
        // White
        Game game = FEN.getGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("e1"), Position.fromAlgebraic("g1"), '\0');
        Move move2 = new Move(Position.fromAlgebraic("e1"), Position.fromAlgebraic("c1"), '\0');
        assertEquals("O-O", MoveUtils.toAlgebraic(move1, game));
        assertEquals("O-O-O", MoveUtils.toAlgebraic(move2, game));

        // Black
        game = FEN.getGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R b KQkq - 0 1", null);
        move1 = new Move(Position.fromAlgebraic("e8"), Position.fromAlgebraic("g8"), '\0');
        move2 = new Move(Position.fromAlgebraic("e8"), Position.fromAlgebraic("c8"), '\0');
        assertEquals("O-O", MoveUtils.toAlgebraic(move1, game));
        assertEquals("O-O-O", MoveUtils.toAlgebraic(move2, game));
    }

    /**
     * Verifies that algebraic notation properly resolves ambiguities when the same piece type can make a move.
     */
    @Test
    public void testAmbiguity() {
        // No Ambiguity
        Game game = FEN.getGame("8/8/4n3/8/8/5r2/8/k1K5 b - - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("e6"), Position.fromAlgebraic("f4"), '\0');
        Move move2 = new Move(Position.fromAlgebraic("f3"), Position.fromAlgebraic("f4"), '\0');
        assertEquals("Nf4", MoveUtils.toAlgebraic(move1, game));
        assertEquals("Rf4", MoveUtils.toAlgebraic(move2, game));

        // Similar File
        game = FEN.getGame("8/4R3/8/8/8/4R3/8/k1K5 w - - 1 1", null);
        move1 = new Move(Position.fromAlgebraic("e3"), Position.fromAlgebraic("e5"), '\0');
        move2 = new Move(Position.fromAlgebraic("e7"), Position.fromAlgebraic("e5"), '\0');
        assertEquals("R3e5", MoveUtils.toAlgebraic(move1, game));
        assertEquals("R7e5", MoveUtils.toAlgebraic(move2, game));

        // Similar Rank
        game = FEN.getGame("8/8/8/2R3R1/8/8/8/k1K5 w - - 2 1", null);
        move1 = new Move(Position.fromAlgebraic("c5"), Position.fromAlgebraic("e5"), '\0');
        move2 = new Move(Position.fromAlgebraic("g5"), Position.fromAlgebraic("e5"), '\0');
        assertEquals("Rce5", MoveUtils.toAlgebraic(move1, game));
        assertEquals("Rge5", MoveUtils.toAlgebraic(move2, game));

        // Both
        game = FEN.getGame("8/8/4Q1Q1/8/4Q1Q1/8/8/k1K5 w - - 3 1", null);
        move1 = new Move(Position.fromAlgebraic("e4"), Position.fromAlgebraic("f5"), '\0');
        move2 = new Move(Position.fromAlgebraic("e6"), Position.fromAlgebraic("f5"), '\0');
        Move move3 = new Move(Position.fromAlgebraic("g4"), Position.fromAlgebraic("f5"), '\0');
        Move move4 = new Move(Position.fromAlgebraic("g6"), Position.fromAlgebraic("f5"), '\0');
        assertEquals("Qe4f5", MoveUtils.toAlgebraic(move1, game));
        assertEquals("Qe6f5", MoveUtils.toAlgebraic(move2, game));
        assertEquals("Qg4f5", MoveUtils.toAlgebraic(move3, game));
        assertEquals("Qg6f5", MoveUtils.toAlgebraic(move4, game));

        // Knights
        game = FEN.getGame("r1bqk2r/1p2ppbp/p1np1np1/2p5/4P3/PBNP3P/1PP2PP1/R1BQK1NR w KQkq - 7 10", null);
        move1 = new Move(Position.fromAlgebraic("g1"), Position.fromAlgebraic("e2"), '\0');
        move2 = new Move(Position.fromAlgebraic("c3"), Position.fromAlgebraic("e2"), '\0');
        assertEquals("Nge2", MoveUtils.toAlgebraic(move1, game));
        assertEquals("Nce2", MoveUtils.toAlgebraic(move2, game));
    }

    /**
     * Ensures that capturing moves are represented correctly in algebraic notation.
     */
    @Test
    public void testCapture() {
        // White
        Game game = FEN.getGame("8/8/8/4p1R1/8/4r1P1/8/k1K5 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("g5"), Position.fromAlgebraic("e5"), '\0');
        assertEquals("Rxe5", MoveUtils.toAlgebraic(move, game));

        // Black
        game = FEN.getGame("8/8/8/4p1R1/8/4r1P1/8/k1K5 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("e3"), Position.fromAlgebraic("g3"), '\0');
        assertEquals("Rxg3", MoveUtils.toAlgebraic(move, game));
    }

    /**
     * Checks that moves which result in a check to the opposing king are correctly annotated in algebraic notation.
     */
    @Test
    public void testChecks() {
        // White
        Game game = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("b5"), Position.fromAlgebraic("a5"), '\0');
        assertEquals("Ra5+", MoveUtils.toAlgebraic(move, game));

        // Black
        game = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("h2"), Position.fromAlgebraic("h1"), '\0');
        assertEquals("Rh1+", MoveUtils.toAlgebraic(move, game));
    }

    /**
     * Verifies that moves which result in a checkmate are correctly indicated in algebraic notation.
     */
    @Test
    public void testCheckmates() {
        // White
        Game game = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("c6"), Position.fromAlgebraic("a6"), '\0');
        assertEquals("Ra6#", MoveUtils.toAlgebraic(move, game));

        // Black
        game = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("g3"), Position.fromAlgebraic("g1"), '\0');
        assertEquals("Rg1#", MoveUtils.toAlgebraic(move, game));
    }

    /**
     * Tests the correct representation of a complex scenario involving promotion, capture, and checkmate in a single move.
     */
    @Test
    public void testPromotion_Capture_Checkmate() {
        // White
        Game game = FEN.getGame("2q2k2/1P5R/8/8/8/8/1p5r/2Q2K2 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("b7"), Position.fromAlgebraic("c8"), 'R');
        assertEquals("bxc8=R#", MoveUtils.toAlgebraic(move, game));

        // Black
        game = FEN.getGame("2q2k2/1P5R/8/8/8/8/1p5r/2Q2K2 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("b2"), Position.fromAlgebraic("c1"), 'R');
        assertEquals("bxc1=R#", MoveUtils.toAlgebraic(move, game));
    }
}

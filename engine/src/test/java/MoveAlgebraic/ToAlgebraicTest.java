package MoveAlgebraic;

import com.nathanholmberg.chess.engine.game.ChessGame;
import com.nathanholmberg.chess.engine.types.Move;
import com.nathanholmberg.chess.engine.types.Position;
import com.nathanholmberg.chess.engine.utils.FEN;
import com.nathanholmberg.chess.engine.utils.MoveUtils;

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
    @Test
    public void testPawn() {
        // White
        ChessGame chessGame = FEN.getGame("8/4kpp1/8/8/8/8/1PPK4/8 w - - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("b2"), Position.fromAlgebraic("b3"), '\0');
        Move move2 = new Move(Position.fromAlgebraic("c2"), Position.fromAlgebraic("c4"), '\0');
        assertEquals("b3", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("c4", MoveUtils.toAlgebraic(move2, chessGame));

        // Black
        chessGame = FEN.getGame("8/4kpp1/8/8/8/8/1PPK4/8 b - - 0 1", null);
        move1 = new Move(Position.fromAlgebraic("g7"), Position.fromAlgebraic("g6"), '\0');
        move2 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f5"), '\0');
        assertEquals("g6", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("f5", MoveUtils.toAlgebraic(move2, chessGame));

        // En Passant
        chessGame = FEN.getGame("r3kb1r/pp2p1pp/3qb3/1PpPP3/4n3/1n1PBPPB/P1Q5/RN2K1NR w KQkq c6 0 16", null);
        move1 = new Move(Position.fromAlgebraic("b5"), Position.fromAlgebraic("c6"), '\0');
        assertEquals(move1, MoveUtils.fromAlgebraic("bxc6", chessGame));
        assertEquals("bxc6", MoveUtils.toAlgebraic(move1, chessGame));
    }

    @Test
    public void testPromotion() {
        // White
        ChessGame chessGame = FEN.getGame("8/5P2/k7/8/8/K7/5p2/8 w - - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'Q');
        Move move2 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'R');
        Move move3 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'N');
        Move move4 = new Move(Position.fromAlgebraic("f7"), Position.fromAlgebraic("f8"), 'B');
        assertEquals("f8=Q", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("f8=R", MoveUtils.toAlgebraic(move2, chessGame));
        assertEquals("f8=N", MoveUtils.toAlgebraic(move3, chessGame));
        assertEquals("f8=B", MoveUtils.toAlgebraic(move4, chessGame));

        // Black
        chessGame = FEN.getGame("8/5P2/k7/8/8/K7/5p2/8 b - - 0 1", null);
        move1 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'Q');
        move2 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'R');
        move3 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'N');
        move4 = new Move(Position.fromAlgebraic("f2"), Position.fromAlgebraic("f1"), 'B');
        assertEquals("f1=Q", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("f1=R", MoveUtils.toAlgebraic(move2, chessGame));
        assertEquals("f1=N", MoveUtils.toAlgebraic(move3, chessGame));
        assertEquals("f1=B", MoveUtils.toAlgebraic(move4, chessGame));
    }

    @Test
    public void testCastling() {
        // White
        ChessGame chessGame = FEN.getGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R w KQkq - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("e1"), Position.fromAlgebraic("g1"), '\0');
        Move move2 = new Move(Position.fromAlgebraic("e1"), Position.fromAlgebraic("c1"), '\0');
        assertEquals("O-O", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("O-O-O", MoveUtils.toAlgebraic(move2, chessGame));

        // Black
        chessGame = FEN.getGame("r3k2r/pppppppp/8/8/8/8/PPPPPPPP/R3K2R b KQkq - 0 1", null);
        move1 = new Move(Position.fromAlgebraic("e8"), Position.fromAlgebraic("g8"), '\0');
        move2 = new Move(Position.fromAlgebraic("e8"), Position.fromAlgebraic("c8"), '\0');
        assertEquals("O-O", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("O-O-O", MoveUtils.toAlgebraic(move2, chessGame));
    }

    @Test
    public void testAmbiguity() {
        // No Ambiguity
        ChessGame chessGame = FEN.getGame("8/8/4n3/8/8/5r2/8/k1K5 b - - 0 1", null);
        Move move1 = new Move(Position.fromAlgebraic("e6"), Position.fromAlgebraic("f4"), '\0');
        Move move2 = new Move(Position.fromAlgebraic("f3"), Position.fromAlgebraic("f4"), '\0');
        assertEquals("Nf4", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("Rf4", MoveUtils.toAlgebraic(move2, chessGame));

        // Similar File
        chessGame = FEN.getGame("8/4R3/8/8/8/4R3/8/k1K5 w - - 1 1", null);
        move1 = new Move(Position.fromAlgebraic("e3"), Position.fromAlgebraic("e5"), '\0');
        move2 = new Move(Position.fromAlgebraic("e7"), Position.fromAlgebraic("e5"), '\0');
        assertEquals("R3e5", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("R7e5", MoveUtils.toAlgebraic(move2, chessGame));

        // Similar Rank
        chessGame = FEN.getGame("8/8/8/2R3R1/8/8/8/k1K5 w - - 2 1", null);
        move1 = new Move(Position.fromAlgebraic("c5"), Position.fromAlgebraic("e5"), '\0');
        move2 = new Move(Position.fromAlgebraic("g5"), Position.fromAlgebraic("e5"), '\0');
        assertEquals("Rce5", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("Rge5", MoveUtils.toAlgebraic(move2, chessGame));

        // Both
        chessGame = FEN.getGame("8/8/4Q1Q1/8/4Q1Q1/8/8/k1K5 w - - 3 1", null);
        move1 = new Move(Position.fromAlgebraic("e4"), Position.fromAlgebraic("f5"), '\0');
        move2 = new Move(Position.fromAlgebraic("e6"), Position.fromAlgebraic("f5"), '\0');
        Move move3 = new Move(Position.fromAlgebraic("g4"), Position.fromAlgebraic("f5"), '\0');
        Move move4 = new Move(Position.fromAlgebraic("g6"), Position.fromAlgebraic("f5"), '\0');
        assertEquals("Qe4f5", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("Qe6f5", MoveUtils.toAlgebraic(move2, chessGame));
        assertEquals("Qg4f5", MoveUtils.toAlgebraic(move3, chessGame));
        assertEquals("Qg6f5", MoveUtils.toAlgebraic(move4, chessGame));

        // Knights
        chessGame = FEN.getGame("r1bqk2r/1p2ppbp/p1np1np1/2p5/4P3/PBNP3P/1PP2PP1/R1BQK1NR w KQkq - 7 10", null);
        move1 = new Move(Position.fromAlgebraic("g1"), Position.fromAlgebraic("e2"), '\0');
        move2 = new Move(Position.fromAlgebraic("c3"), Position.fromAlgebraic("e2"), '\0');
        assertEquals("Nge2", MoveUtils.toAlgebraic(move1, chessGame));
        assertEquals("Nce2", MoveUtils.toAlgebraic(move2, chessGame));
    }

    @Test
    public void testCapture() {
        // White
        ChessGame chessGame = FEN.getGame("8/8/8/4p1R1/8/4r1P1/8/k1K5 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("g5"), Position.fromAlgebraic("e5"), '\0');
        assertEquals("Rxe5", MoveUtils.toAlgebraic(move, chessGame));

        // Black
        chessGame = FEN.getGame("8/8/8/4p1R1/8/4r1P1/8/k1K5 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("e3"), Position.fromAlgebraic("g3"), '\0');
        assertEquals("Rxg3", MoveUtils.toAlgebraic(move, chessGame));
    }

    @Test
    public void testChecks() {
        // White
        ChessGame chessGame = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("b5"), Position.fromAlgebraic("a5"), '\0');
        assertEquals("Ra5+", MoveUtils.toAlgebraic(move, chessGame));

        // Black
        chessGame = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("h2"), Position.fromAlgebraic("h1"), '\0');
        assertEquals("Rh1+", MoveUtils.toAlgebraic(move, chessGame));
    }

    @Test
    public void testCheckmates() {
        // White
        ChessGame chessGame = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("c6"), Position.fromAlgebraic("a6"), '\0');
        assertEquals("Ra6#", MoveUtils.toAlgebraic(move, chessGame));

        // Black
        chessGame = FEN.getGame("8/8/2R5/1R6/8/k5r1/7r/3K4 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("g3"), Position.fromAlgebraic("g1"), '\0');
        assertEquals("Rg1#", MoveUtils.toAlgebraic(move, chessGame));
    }

    @Test
    public void testPromotion_Capture_Checkmate() {
        // White
        ChessGame chessGame = FEN.getGame("2q2k2/1P5R/8/8/8/8/1p5r/2Q2K2 w - - 1 2", null);
        Move move = new Move(Position.fromAlgebraic("b7"), Position.fromAlgebraic("c8"), 'R');
        assertEquals("bxc8=R#", MoveUtils.toAlgebraic(move, chessGame));

        // Black
        chessGame = FEN.getGame("2q2k2/1P5R/8/8/8/8/1p5r/2Q2K2 b - - 1 2", null);
        move = new Move(Position.fromAlgebraic("b2"), Position.fromAlgebraic("c1"), 'R');
        assertEquals("bxc1=R#", MoveUtils.toAlgebraic(move, chessGame));
    }
}

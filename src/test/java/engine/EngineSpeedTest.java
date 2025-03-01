package engine;

import engine.game.Game;
import engine.types.Position;
import engine.game.Timer;
import engine.utils.FEN;

import java.util.List;

/**
 * A utility for benchmarking the performance of the chess engine's move generation.
 * Measures the time taken to compute legal moves from a specific chessboard position.
 *
 * <p>Initializes a game state using a FEN string, calculates legal moves from
 * a given position, and outputs the results and execution time.</p>
 */
public class EngineSpeedTest {
    public static void main(String[] args) {
        // Setup Game
        Game game = FEN.getGame("4k3/8/8/8/1Q4K1/8/8/8 w KQkq - 0 1", new Timer(600_000, 0));

        // Suggest garbage collection
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException ignored) { }

        // Run Test
        long startTime = System.nanoTime();
        List<Position> positions = game.getLegalMoves(Position.fromAlgebraic("b4"));
        long endTime = System.nanoTime();

        // Print
        System.out.println(positions);
        System.out.println("Execution time (ms): " + (endTime - startTime) / 1_000_000.0);
    }
}

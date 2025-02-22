package tests;

import engine.game.Game;
import engine.utils.Position;
import engine.game.Timer;

import java.util.List;

public class EngineSpeedTest {
    public static void main(String[] args) {
        // Setup Game
        Game game = Game.fromFEN("4k3/8/8/8/1Q4K1/8/8/8 w KQkq - 0 1", new Timer(600_000, 0));

        // Suggest garbage collection
        System.gc();
        try { Thread.sleep(100); } catch (InterruptedException ignored) { }

        // Run Test
        long startTime = System.nanoTime();
        List<Position> positions = game.getLegalMoves(Position.fromAlgebraic("b4"));
        long endTime = System.nanoTime();

        // Sout
        System.out.println(positions);
        System.out.println("Execution time (ms): " + (endTime - startTime) / 1_000_000.0);
    }
}
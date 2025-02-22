package tests;

import engine.Game;
import engine.board.Position;
import engine.utils.Timer;
import utils.Color;

import java.util.List;

public class EngineSpeedTest {
    public static void main(String[] args) {
        // Setup Game
        Game game = Game.fromFEN("4k3/8/8/8/1Q4K1/8/8/8 w KQkq - 0 1", new Timer(600_000, 0, Color.WHITE));

        System.gc(); // Suggest garbage collection
        try { Thread.sleep(100); } catch (InterruptedException ignored) { } // Allow GC to settle

        long startTime = System.nanoTime();
        List<Position> positions = game.getLegalMoves(Position.fromAlgebraic("b4"));
        long endTime = System.nanoTime();
        System.out.println(positions);

        System.out.println("Execution time (ms): " + (endTime - startTime) / 1_000_000.0);
    }
}
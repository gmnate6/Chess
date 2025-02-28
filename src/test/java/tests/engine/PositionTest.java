package tests.engine;

import engine.types.Position;

import org.junit.*;
import static org.junit.Assert.*;

public class PositionTest {
    @Test
    public void Constructor() {
        // Correct Constructor
        int file = 5;
        int rank = 3;
        Position pos = new Position(file, rank);

        assertEquals(file, pos.file());
        assertEquals(rank, pos.rank());

        // Incorrect Constructor
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, 8));
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 8));
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, -1));
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, -1));
    }

    @Test
    public void algebraicTest() {
        // Check From Algebraic
        Position pos1 = new Position(0, 0);
        Position pos2 = Position.fromAlgebraic("a1");
        assertEquals(pos1, pos2);

        // Check To Algebraic
        Position pos3 = Position.fromAlgebraic("h8");
        assertEquals("h8", pos3.toAlgebraic());
    }

    @ Test public void toCharTest() {
        Position pos = new Position(0, 0);
        assertEquals('a', pos.fileToChar());
        assertEquals('1', pos.rankToChar());
    }

    @Test
    public void moveTest() {
        Position pos1 = Position.fromAlgebraic("a1");
        Position pos2 = pos1.move(1, 1);
        assertEquals("b2", pos2.toAlgebraic());
    }

    @Test public void equalsTest() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(0, 0);
        assertEquals(pos1, pos2);
    }
}

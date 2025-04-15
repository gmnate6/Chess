import com.nathanholmberg.chess.engine.exceptions.IllegalNotationException;
import com.nathanholmberg.chess.engine.exceptions.IllegalPositionException;
import com.nathanholmberg.chess.engine.types.Position;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <p><strong>Purpose:</strong></p>
 * <p>This class contains a suite of unit tests for the <code>Position</code> class, which represents
 * a position on the chessboard using file and rank coordinates.</p>
 *
 * <p><strong>Functionalities Tested:</strong></p>
 * <ul>
 *   <li><strong>Instantiation:</strong> Validates the proper instantiation of <code>Position</code> objects,
 *   including testing with both valid and invalid inputs.</li>
 *   <li><strong>Algebraic Notation:</strong> Ensures correct conversion between algebraic notation
 *   (e.g., "a1", "h8") and the internal representation of positions.</li>
 *   <li><strong>Character Conversion:</strong> Tests the conversion of file and rank values
 *   to their respective characters (e.g., 'a', '1').</li>
 *   <li><strong>Position Movement:</strong> Validates offsets applied to a position to simulate
 *   movement on the chessboard.</li>
 *   <li><strong>Equality Testing:</strong> Confirms the correctness of equality checks between
 *   <code>Position</code> objects.</li>
 * </ul>
 *
 * <p><strong>Significance:</strong></p>
 * <p>These tests ensure the reliability and robustness of the <code>Position</code> class, verifying its
 * functionality in critical operations essential for chessboard representation and manipulation.</p>
 */
public class PositionTest {
    @Test
    public void constructorTest() {
        // Correct Constructor
        int file = 5;
        int rank = 3;
        Position pos = new Position(file, rank);

        assertEquals(file, pos.file());
        assertEquals(rank, pos.rank());

        // Incorrect Constructor
        assertThrows(IllegalPositionException.class, () -> new Position(8, 0));
        assertThrows(IllegalPositionException.class, () -> new Position(0, 8));
        assertThrows(IllegalPositionException.class, () -> new Position(8, 8));
        assertThrows(IllegalPositionException.class, () -> new Position(-1, 0));
        assertThrows(IllegalPositionException.class, () -> new Position(0, -1));
        assertThrows(IllegalPositionException.class, () -> new Position(-1, -1));
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

        // Incorrect Constructor
        assertThrows(IllegalNotationException.class, () -> Position.fromAlgebraic("q1"));
        assertThrows(IllegalNotationException.class, () -> Position.fromAlgebraic("a0"));
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

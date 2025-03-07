package engine.game;

import utils.Color;

/**
 * Represents a chess game timer that tracks and manages time for both players.
 * This class provides features such as time control per player, time increments after each move,
 * and switching turns while tracking elapsed time.
 *
 * <p>Key Features:</p>
 * <ul>
 *   <li>Tracks individual time remaining for White and Black players in milliseconds.</li>
 *   <li>Supports optional time increments added to the player's clock after each move.</li>
 *   <li>Provides functionality to initialize timers with custom settings or intermediate game states.</li>
 *   <li>Ensures accurate time tracking using system timestamps during active gameplay.</li>
 *   <li>Allows switching turns, starting the timer, and checking if a player has run out of time.</li>
 *   <li>Includes utility methods for retrieving formatted time (`MM:SS`) and creating a deep copy of the timer.</li>
 * </ul>
 *
 * <p>This class assumes a standard chess game setup with two players, represented by `Color.WHITE` and
 * `Color.BLACK`, and allows flexible initialization for various game states or time control systems.</p>
 *
 * <p>Typical usage includes starting the timer, switching turns, fetching remaining time,
 * and checking time-related conditions during a game.</p>
 */
public class Timer {
    private long whiteTime; // In milliseconds
    private long blackTime;
    private long lastMoveTimestamp;
    private Color turn;
    private final long increment; // Time increment per move (if applicable)
    private boolean started = false;

    /**
     * Constructs a Timer for a chess game with the specified initial time for both players
     * and an optional increment per move. This constructor assumes White starts the game.
     *
     * @param initialTime The initial time (in milliseconds) assigned to both players at the start of the game.
     *                    For example, 10 minutes = 600,000 milliseconds.
     * @param increment   The additional time (in milliseconds) added to a player's clock after each move.
     *                    For example, 5 seconds = 5,000 milliseconds.
     */
    public Timer(long initialTime, long increment) {
        this.whiteTime = initialTime;
        this.blackTime = initialTime;
        this.increment = increment;
        this.turn = Color.WHITE;
    }

    /**
     * Constructs a Timer with specific remaining times for both players, an optional increment,
     * and a specified starting turn. This constructor allows more fine-grained control over
     * the Timer, such as initializing games from an intermediate state or custom scenarios.
     *
     * @param whiteTime    The remaining time (in milliseconds) for the White player.
     *                     For example, 5 minutes = 300,000 milliseconds.
     * @param blackTime    The remaining time (in milliseconds) for the Black player.
     * @param increment    The additional time (in milliseconds) added to a player's clock after each move.
     *                     For example, 10 seconds = 10,000 milliseconds.
     * @param turn  Specifies the player who starts the game, either `Color.WHITE` or `Color.BLACK`.
     */
    public Timer(long whiteTime, long blackTime, long increment, Color turn) {
        this.whiteTime = whiteTime;
        this.blackTime = blackTime;
        this.increment = increment;
        this.turn = turn;
    }

    /**
     * Creates and returns a deep copy of the current Timer object.
     *
     * <p>The deep copy ensures that all fields, including time values and
     * game state (e.g., current turn, started state, etc.), are duplicated to
     * create a completely independent Timer instance. Modifications to the
     * copied Timer do not affect the original Timer and vice versa.</p>
     *
     * @return A new Timer instance that is a deep copy of this Timer.
     */
    public Timer getDeepCopy() {
        Timer copy = new Timer(whiteTime, blackTime, increment, turn);
        copy.lastMoveTimestamp = lastMoveTimestamp;
        copy.started = started;
        return copy;
    }

    /**
     * Starts the timer by setting the `lastMoveTimestamp` to the current system time.
     * Also marks the timer as started.
     */
    public void start() {
        this.lastMoveTimestamp = System.currentTimeMillis();
        started = true;
    }

    /**
     * Returns the formatted time remaining for the specified player.
     * The time is returned in the format `MM:SS` (minutes:seconds).
     *
     * @param player The player (Color.WHITE or Color.BLACK) whose remaining time is requested.
     * @return A formatted string showing time left in `MM:SS` format.
     */
    public String getFormatedTimeLeft(Color player) {
        long timeInMillis = getTimeLeft(player);
        long minutes = timeInMillis / 60000; // 60,000 milliseconds in a minute
        long seconds = (timeInMillis % 60000) / 1000; // Remainder of seconds after minutes

        return String.format("%02d:%02d", minutes, seconds); // Format as MM:SS
    }

    /**
     * Switches the timer to the next player's turn, deducts elapsed time from the current player,
     * and adds the increment time to their clock.
     */
    public void switchTurn() {
        if (!started) { return; }

        // Calc
        long now = System.currentTimeMillis();
        long elapsed = now - lastMoveTimestamp;

        // Update Time Stamp
        if (turn == Color.WHITE) {
            whiteTime = Math.max(0, whiteTime - elapsed + increment);
        } else {
            blackTime = Math.max(0, blackTime - elapsed + increment);
        }
        lastMoveTimestamp = now;

        // Switch Turn
        turn = turn.inverse();
    }

    // Getters
    public boolean isStarted() { return started; }
    public boolean isOutOfTime(Color player) {
        return (player == Color.WHITE ? whiteTime : blackTime) <= 0;
    }
    public long getTimeLeft(Color player) {
        return player == Color.WHITE ? whiteTime : blackTime;
    }
    public Color getTurn() { return turn; }

    // Setters
    public void setTurn(Color currentTurn) { this.turn = currentTurn; }

    @Override
    public String toString() {
        return "White Time: " + getFormatedTimeLeft(Color.WHITE) + "\nBlack Time: " + getFormatedTimeLeft(Color.BLACK);
    }
}

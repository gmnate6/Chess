package engine.utils;

import utils.Color;

/**
 * The `Timer` class provides functionality to manage chess timers for both players.
 * It tracks the remaining time for White and Black players, handles time increments,
 * and manages the state of the timer during the game, including turn switching.
 *
 * This class supports initializing timers with a specific starting time and increment,
 * switching turns, and checking if a player is out of time.
 *
 * @author [Your Name]
 */
public class Timer {
    private long whiteTime; // In milliseconds
    private long blackTime;
    private long lastMoveTimestamp;
    private Color currentTurn;
    private final long increment; // Time increment per move (if applicable)
    boolean started = false;

    /**
     * Constructs a Timer for a chess game with specified initial time, increment per move,
     * and starting player. This constructor allows customization of both players' starting
     * time and determines which player begins the game.
     *
     * @param initialTime The initial time (in milliseconds) assigned to both players at the
     *                    start of the game (e.g., 10 minutes = 600,000 milliseconds).
     * @param increment   The additional time (in milliseconds) added to a player's clock
     *                    after each move (e.g., 5 seconds = 5,000 milliseconds).
     * @param currentTurn Specifies the player who starts the game, either `Color.WHITE` or
     *                    `Color.BLACK`.
     */
    public Timer(long initialTime, long increment, Color currentTurn) {
        this.whiteTime = initialTime;
        this.blackTime = initialTime;
        this.increment = increment;
        this.currentTurn = currentTurn;
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
        long now = System.currentTimeMillis();
        long elapsed = now - lastMoveTimestamp;

        if (currentTurn == Color.WHITE) {
            whiteTime = Math.max(0, whiteTime - elapsed + increment);
        } else {
            blackTime = Math.max(0, blackTime - elapsed + increment);
        }

        lastMoveTimestamp = now;
        currentTurn = (currentTurn == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // Getters
    public boolean isStarted() { return started; }
    public boolean isOutOfTime(Color player) {
        return (player == Color.WHITE ? whiteTime : blackTime) <= 0;
    }
    public long getTimeLeft(Color player) {
        return player == Color.WHITE ? whiteTime : blackTime;
    }
    public Color getCurrentTurn() { return currentTurn; }
}

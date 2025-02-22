package engine.utils;

import utils.Color;

// TODO: Figure out how reconnecting with FEN will affect the timer
public class Timer {
    private long whiteTime; // In milliseconds
    private long blackTime;
    private long lastMoveTimestamp;
    private Color currentTurn;
    private long increment; // Time increment per move (if applicable)

    public Timer(long initialTime, long increment) {
        this.whiteTime = initialTime;
        this.blackTime = initialTime;
        this.increment = increment;
        this.lastMoveTimestamp = System.currentTimeMillis();
        this.currentTurn = Color.WHITE; // Assuming White starts
    }

    public void setState(long whiteTime, long blackTime, long increment, Color currentTurn, long lastMoveTimestamp) {
        this.whiteTime = whiteTime;
        this.blackTime = blackTime;
        this.increment = increment;
        this.currentTurn = currentTurn;
        this.lastMoveTimestamp = lastMoveTimestamp;
    }

    public String getFormatedTimeLeft(Color player) {
        long timeInMillis = getTimeLeft(player);
        long minutes = timeInMillis / 60000; // 60,000 milliseconds in a minute
        long seconds = (timeInMillis % 60000) / 1000; // Remainder of seconds after minutes

        return String.format("%02d:%02d", minutes, seconds); // Format as MM:SS
    }

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

    public boolean isOutOfTime(Color player) {
        return (player == Color.WHITE ? whiteTime : blackTime) <= 0;
    }

    public long getTimeLeft(Color player) {
        return player == Color.WHITE ? whiteTime : blackTime;
    }
}

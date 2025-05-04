package com.nathanholmberg.chess.engine.game;

import com.nathanholmberg.chess.engine.enums.Color;

// Note: Time is measured in milliseconds
public class ChessTimer {
    private long initialTime;
    private long increment;

    private long whiteTime;
    private long blackTime;
    private long lastMoveTimestamp;
    private Color turn;
    private boolean active = false;

    public ChessTimer(long initialTime, long increment) {
        this.initialTime = initialTime;
        this.whiteTime = initialTime;
        this.blackTime = initialTime;
        this.increment = increment;
        this.turn = Color.WHITE;
    }

    public void start() {
        if (active) { return; }
        this.lastMoveTimestamp = System.currentTimeMillis();
        active = true;
    }

    public void stop() {
        // Last Update
        updateTimeStamp();
        active = false;
    }

    public String getFormatedTimeLeft(Color player) {
        long timeInMillis = getTimeLeft(player);
        long minutes = timeInMillis / 60000; // 60,000 milliseconds in a minute
        long seconds = (timeInMillis % 60000) / 1000; // Remainder of seconds after minutes

        return String.format("%02d:%02d", minutes, seconds); // Format as MM:SS
    }

    private void updateTimeStamp() {
        if (!active) { return; }

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
    }

    public void switchTurn() {
        if (!active) { return; }

        // Update Time Stamp
        updateTimeStamp();

        // Switch Turn
        turn = turn.inverse();
    }

    public boolean isOutOfTime(Color player) {
        return (player == Color.WHITE ? whiteTime : blackTime) <= 0;
    }

    public long getTimeLeft(Color player) {
        updateTimeStamp();
        return player == Color.WHITE ? whiteTime : blackTime;
    }

    public Color getTurn() {
        return turn;
    }

    public boolean isActive() {
        return active;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public long getIncrement() {
        return increment;
    }

    public void setIncrement(long increment) {
        this.increment = increment;
    }

    public void setInitialTime(long initialTime) {
        this.initialTime = initialTime;
    }

    public void setWhiteTime(long whiteTime) {
        this.whiteTime = whiteTime;
    }

    public void setBlackTime(long blackTime) {
        this.blackTime = blackTime;
    }

    @Override
    public String toString() {
        return "White Time: " + getFormatedTimeLeft(Color.WHITE) + "\nBlack Time: " + getFormatedTimeLeft(Color.BLACK);
    }
}

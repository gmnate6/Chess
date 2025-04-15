package com.nathanholmberg.chess.engine.game;

import com.nathanholmberg.chess.engine.enums.Color;

public class ChessTimer {
    private long whiteTime; // In milliseconds
    private long blackTime;
    private long lastMoveTimestamp;
    private Color turn;
    private final long increment; // Time increment per move (if applicable)
    private boolean active = false;

    public ChessTimer(long initialTime, long increment) {
        this.whiteTime = initialTime;
        this.blackTime = initialTime;
        this.increment = increment;
        this.turn = Color.WHITE;
    }

    public ChessTimer(long whiteTime, long blackTime, long increment, Color turn) {
        this.whiteTime = whiteTime;
        this.blackTime = blackTime;
        this.increment = increment;
        this.turn = turn;
    }

    public ChessTimer getDeepCopy() {
        ChessTimer copy = new ChessTimer(whiteTime, blackTime, increment, turn);
        copy.lastMoveTimestamp = lastMoveTimestamp;
        copy.active = active;
        return copy;
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

    // Getters
    public boolean isOutOfTime(Color player) {
        return (player == Color.WHITE ? whiteTime : blackTime) <= 0;
    }
    public long getTimeLeft(Color player) {
        updateTimeStamp();
        return player == Color.WHITE ? whiteTime : blackTime;
    }
    public Color getTurn() { return turn; }
    public boolean isActive() { return active; }

    // Setters
    public void setTurn(Color currentTurn) { this.turn = currentTurn; }

    @Override
    public String toString() {
        return "White Time: " + getFormatedTimeLeft(Color.WHITE) + "\nBlack Time: " + getFormatedTimeLeft(Color.BLACK);
    }
}

package com.nathanholmberg.chess.engine.game;

import com.nathanholmberg.chess.engine.enums.Color;

// Note: Time is measured in milliseconds
public class ChessTimer {
    private final long initialTime;
    private final long increment;

    private long whiteTime;
    private long blackTime;
    private long lastMoveTimestamp;
    private Color turn;

    private boolean isActive = false;
    private Thread timerThread;

    private Listener listener;
    public interface Listener {
        void onTimerStarted(ChessTimer timer);
        void onTimerUpdate(ChessTimer timer);
        void onTimeUp(Color player);
        void onTimerStopped(ChessTimer timer);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public ChessTimer(long initialTime, long increment) {
        this.initialTime = initialTime;
        this.whiteTime = initialTime;
        this.blackTime = initialTime;
        this.increment = increment;
        this.turn = Color.WHITE;
    }

    public void start() {
        if (isActive) { return; }

        this.lastMoveTimestamp = System.currentTimeMillis();
        isActive = true;

        // Start Timer Thread
        timerThread = new Thread(() -> {
            while (isActive) {
                try {
                    updateTimeStamp();
                    checkForTimeout();

                    if (listener != null ) {
                        listener.onTimerUpdate(this);
                    }

                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        timerThread.setDaemon(true);
        timerThread.start();

        if (listener != null ) {
            listener.onTimerStarted(this);
        }
    }

    public void stop() {
        updateTimeStamp();
        isActive = false;
        timerThread.interrupt();

        if (listener != null ) {
            listener.onTimerStopped(this);
        }
    }

    private synchronized void updateTimeStamp() {
        if (!isActive) { return; }

        // Calc
        long now = System.currentTimeMillis();
        long elapsed = now - lastMoveTimestamp;

        // Update Time Stamp
        if (turn == Color.WHITE) {
            whiteTime = Math.max(0, whiteTime - elapsed);
        } else {
            blackTime = Math.max(0, blackTime - elapsed);
        }
        lastMoveTimestamp = now;
    }

    private void checkForTimeout() {
        Color result = null;
        if (isOutOfTime(Color.WHITE)) {
            result = Color.WHITE;
        } else if (isOutOfTime(Color.BLACK)) {
            result = Color.BLACK;
        }

        if (result != null && listener != null ) {
            listener.onTimeUp(result);
            stop();
        }
    }

    public void switchTurn() {
        if (!isActive) { return; }

        updateTimeStamp();
        checkForTimeout();

        // Timer might have been stopped if a timeout occurred
        if (!isActive) {
            return; // Stop further processing
        }

        // Add Time Increment
        if (turn == Color.WHITE) {
            whiteTime += increment;
        } else {
            blackTime += increment;
        }

        turn = turn.inverse();
    }

    public Color getTurn() {
        return turn;
    }

    public boolean isActive() {
        return isActive;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public long getIncrement() {
        return increment;
    }

    public boolean isOutOfTime(Color player) {
        return (player == Color.WHITE ? whiteTime : blackTime) <= 0;
    }

    public long getTimeLeft(Color player) {
        updateTimeStamp();
        return player == Color.WHITE ? whiteTime : blackTime;
    }

    public long setWhiteTime(long whiteTime) {
        this.whiteTime = whiteTime;
        return this.whiteTime;
    }

    public long setBlackTime(long blackTime) {
        this.blackTime = blackTime;
        return this.blackTime;
    }

    public String getFormatedTimeLeft(Color player) {
        long timeInMillis = getTimeLeft(player);
        long minutes = timeInMillis / 60000; // 60,000 milliseconds in a minute
        long seconds = (timeInMillis % 60000) / 1000; // Remainder of seconds after minutes

        return String.format("%02d:%02d", minutes, seconds); // Format as MM:SS
    }
}

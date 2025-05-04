package com.nathanholmberg.chess.protocol.messages.game.server;

import com.nathanholmberg.chess.protocol.messages.Message;

public class GameStartMessage extends Message {
    private final long initialTime;
    private final long increment;

    public GameStartMessage(long initialTime, long increment) {
        this.initialTime = initialTime;
        this.increment = increment;
    }

    public long getInitialTime() {
        return initialTime;
    }

    public long getIncrement() {
        return increment;
    }
}

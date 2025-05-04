package com.nathanholmberg.chess.protocol.messages.game.server;

import com.nathanholmberg.chess.protocol.messages.Message;

public class ClockUpdateMessage extends Message {
    private final long whiteTime;
    private final long blackTime;

    public ClockUpdateMessage(int whiteTime, int blackTime) {
        this.whiteTime = whiteTime;
        this.blackTime = blackTime;
    }

    public long getWhiteTime() {
        return whiteTime;
    }

    public long getBlackTime() {
        return blackTime;
    }
}

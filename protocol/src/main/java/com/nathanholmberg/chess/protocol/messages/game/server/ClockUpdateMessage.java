package com.nathanholmberg.chess.protocol.messages.game.server;

import com.nathanholmberg.chess.protocol.messages.Message;

public class ClockUpdateMessage extends Message {
    private final int whiteTime;
    private final int blackTime;

    public ClockUpdateMessage(int whiteTime, int blackTime) {
        this.whiteTime = whiteTime;
        this.blackTime = blackTime;
    }

    public int getWhiteTime() {
        return whiteTime;
    }

    public int getBlackTime() {
        return blackTime;
    }
}

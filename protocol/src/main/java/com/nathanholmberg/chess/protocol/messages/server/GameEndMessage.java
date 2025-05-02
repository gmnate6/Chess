package com.nathanholmberg.chess.protocol.messages.server;

import com.nathanholmberg.chess.protocol.messages.Message;

public class GameEndMessage extends Message {
    private final String result;

    public GameEndMessage(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }
}

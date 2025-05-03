package com.nathanholmberg.chess.protocol.messages.game.server;

import com.nathanholmberg.chess.engine.enums.GameResult;
import com.nathanholmberg.chess.protocol.messages.Message;

public class GameEndMessage extends Message {
    private final GameResult result;

    public GameEndMessage(GameResult result) {
        this.result = result;
    }

    public GameResult getResult() {
        return result;
    }
}

package com.nathanholmberg.chess.engine.exceptions;

public class IllegalMoveException extends RuntimeException {
    public IllegalMoveException(String message) {
        super(message);
    }
}

package com.nathanholmberg.chess.engine.exceptions;

public class IllegalPositionException extends RuntimeException {
    public IllegalPositionException(String message) {
        super(message);
    }
}

package com.nathanholmberg.chess.protocol.exceptions;

public class InvalidMessageException extends ProtocolException {
    public InvalidMessageException(String message) {
        super(message);
    }
}

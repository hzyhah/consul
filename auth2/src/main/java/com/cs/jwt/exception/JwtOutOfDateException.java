package com.cs.jwt.exception;

public class JwtOutOfDateException extends Exception {
    public JwtOutOfDateException() {
        super();
    }

    public JwtOutOfDateException(String message) {
        super(message);
    }
}

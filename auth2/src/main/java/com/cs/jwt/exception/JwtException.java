package com.cs.jwt.exception;

public class JwtException extends Exception {
    public JwtException(String message) {
        super(message);
    }

    public JwtException() {
        super();
    }
}

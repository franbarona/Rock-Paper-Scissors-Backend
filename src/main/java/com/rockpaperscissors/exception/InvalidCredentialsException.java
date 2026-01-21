package com.rockpaperscissors.exception;

public class InvalidCredentialsException extends CustomException {
    public InvalidCredentialsException() {
        super("Invalid email or password", "INVALID_CREDENTIALS");
    }
}
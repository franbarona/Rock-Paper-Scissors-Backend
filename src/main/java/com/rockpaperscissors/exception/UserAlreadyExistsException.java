package com.rockpaperscissors.exception;

public class UserAlreadyExistsException extends CustomException {
    public UserAlreadyExistsException(String field, String value) {
        super("User already exists with " + field + ": " + value, "USER_ALREADY_EXISTS");
    }
}
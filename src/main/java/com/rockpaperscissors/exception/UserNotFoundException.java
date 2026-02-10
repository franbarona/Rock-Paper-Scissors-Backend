package com.rockpaperscissors.exception;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String data) {
        super("User not found with: " + data, "USER_NOT_FOUND");
    }
}
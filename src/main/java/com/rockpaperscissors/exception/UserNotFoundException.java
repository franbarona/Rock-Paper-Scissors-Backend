package com.rockpaperscissors.exception;

public class UserNotFoundException extends CustomException {
    public UserNotFoundException(String email) {
        super("User not found with email: " + email, "USER_NOT_FOUND");
    }
}
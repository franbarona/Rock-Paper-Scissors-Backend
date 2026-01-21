package com.rockpaperscissors.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    
    private final String errorCode;
    
    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public CustomException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
}
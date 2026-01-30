package com.rockpaperscissors.exception;

public class StatisticsNotFoundException extends CustomException {
    public StatisticsNotFoundException(Long userId) {
        super("Statistics not found for user ID: " + userId, "STATISTICS_NOT_FOUND");
    }
}
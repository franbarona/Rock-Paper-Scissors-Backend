package com.rockpaperscissors.exception;

import java.util.UUID;

public class StatisticsNotFoundException extends CustomException {
    public StatisticsNotFoundException(UUID userId) {
        super("Statistics not found for user ID: " + userId, "STATISTICS_NOT_FOUND");
    }
}
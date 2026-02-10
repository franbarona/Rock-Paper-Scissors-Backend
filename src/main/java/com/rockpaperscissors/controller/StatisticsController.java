package com.rockpaperscissors.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rockpaperscissors.dto.response.ApiResponse;
import com.rockpaperscissors.dto.response.UserStatisticsResponse;
import com.rockpaperscissors.service.StatisticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final StatisticsService statisticsService;

    @GetMapping("/my-stats")
    public ResponseEntity<ApiResponse<UserStatisticsResponse>> getMyStatistics(
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        UserStatisticsResponse response = statisticsService.getUserStatistics(userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Statistics retrieved successfully"));
    }
}

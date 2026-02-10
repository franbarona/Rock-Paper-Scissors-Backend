package com.rockpaperscissors.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rockpaperscissors.dto.request.GamePlayRequest;
import com.rockpaperscissors.dto.response.ApiResponse;
import com.rockpaperscissors.dto.response.GamePlayResponse;
import com.rockpaperscissors.service.GameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping("/play")
    public ResponseEntity<ApiResponse<GamePlayResponse>> playMove(
            Authentication authentication,
            @RequestBody GamePlayRequest request) {
        UUID userId =  UUID.fromString(authentication.getName());
        GamePlayResponse response = gameService.playMove(request.getMove(), userId);
        return ResponseEntity.ok(ApiResponse.success(response, "Game played successfully"));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<GamePlayResponse>>> getHistory(
            Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        List<GamePlayResponse> history = gameService.getHistory(userId);
        return ResponseEntity.ok(ApiResponse.success(history, "Game history retrieved successfully"));
    }
}
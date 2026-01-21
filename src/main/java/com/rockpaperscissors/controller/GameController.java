package com.rockpaperscissors.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rockpaperscissors.dto.request.GamePlayRequest;
import com.rockpaperscissors.dto.response.ApiResponse;
import com.rockpaperscissors.dto.response.GamePlayResponse;
import com.rockpaperscissors.service.GameService;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/play")
    public ResponseEntity<ApiResponse<GamePlayResponse>> playMove(
            Authentication authentication,
            @RequestBody GamePlayRequest request) {
        String email = authentication.getName();
        GamePlayResponse response = gameService.playMove(request.getMove(), email);
        return ResponseEntity.ok(ApiResponse.success(response, "Game played successfully"));
    }

    @GetMapping("/history")
    public ResponseEntity<ApiResponse<List<GamePlayResponse>>> getHistory(
            Authentication authentication) {
        String email = authentication.getName();
        List<GamePlayResponse> history = gameService.getHistory(email);
        return ResponseEntity.ok(ApiResponse.success(history, "Game history retrieved successfully"));
    }
}
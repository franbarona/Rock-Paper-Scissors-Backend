package com.rockpaperscissors.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.rockpaperscissors.dto.request.GamePlayRequest;
import com.rockpaperscissors.entity.GameResult;
import com.rockpaperscissors.service.GameService;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/play")
    public ResponseEntity<GameResult> playMove(
            Authentication authentication,
            @RequestBody GamePlayRequest request) {
        String userEmail = authentication.getName();
        GameResult result = gameService.playMove(request.getMove(), userEmail);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/history")
    public ResponseEntity<List<GameResult>> getHistory(
            Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(gameService.getHistory(userEmail));
    }
}
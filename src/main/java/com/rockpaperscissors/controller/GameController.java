package com.rockpaperscissors.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rockpaperscissors.dto.GamePlayRequest;
import com.rockpaperscissors.model.GameResult;
import com.rockpaperscissors.service.GameService;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/play")
    public ResponseEntity<GameResult> playMove(@RequestBody GamePlayRequest request) {
        GameResult result = gameService.playMove(request.getMove());
        return ResponseEntity.ok(result);
    }
}
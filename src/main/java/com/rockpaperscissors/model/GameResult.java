package com.rockpaperscissors.model;

import java.time.LocalDateTime;

public class GameResult {
    private Long id;
    private GameMove playerMove;
    private GameMove computerMove;
    private String result; // "WIN", "LOSE", "DRAW"
    private LocalDateTime playedAt;

    public GameResult(GameMove playerMove, GameMove computerMove, String result) {
        this.playerMove = playerMove;
        this.computerMove = computerMove;
        this.result = result;
        this.playedAt = LocalDateTime.now();
    }

    // Getters
    public GameMove getPlayerMove() {
        return playerMove;
    }

    public GameMove getComputerMove() {
        return computerMove;
    }

    public String getResult() {
        return result;
    }

    public LocalDateTime getPlayedAt() {
        return playedAt;
    }
}
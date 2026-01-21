package com.rockpaperscissors.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.rockpaperscissors.model.GameMove;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "game_results")
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameMove playerMove;

    @Enumerated(EnumType.STRING)
    private GameMove computerMove;

    private String result;

    @CreationTimestamp
    private LocalDateTime playedAt;

    public GameResult() {
    }

    public GameResult(GameMove playerMove, GameMove computerMove, String result) {
        this.playerMove = playerMove;
        this.computerMove = computerMove;
        this.result = result;
    }

    // Getters
    public Long getId() {
        return id;
    }

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
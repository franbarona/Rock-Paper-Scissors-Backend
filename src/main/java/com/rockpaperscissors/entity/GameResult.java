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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public GameResult() {
    }

    public GameResult(GameMove playerMove, GameMove computerMove, String result, User user) {
        this.playerMove = playerMove;
        this.computerMove = computerMove;
        this.result = result;
        this.user = user;
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

    public User getUser() {
        return user;
    }
}
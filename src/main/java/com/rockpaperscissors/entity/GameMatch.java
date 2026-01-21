package com.rockpaperscissors.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.rockpaperscissors.enums.GameMove;
import com.rockpaperscissors.enums.GameResult;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "game_matches")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameMatch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GameMove playerMove;

    @Enumerated(EnumType.STRING)
    private GameMove computerMove;

    @Enumerated(EnumType.STRING)
    private GameResult result;

    @CreationTimestamp
    private LocalDateTime playedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
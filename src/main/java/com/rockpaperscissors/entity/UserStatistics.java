package com.rockpaperscissors.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statistics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatistics {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "TEXT")
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "total_matches")
    @Builder.Default
    private Integer totalMatches = 0;

    @Column(name = "total_wins")
    @Builder.Default
    private Integer totalWins = 0;

    @Column(name = "total_losses")
    @Builder.Default
    private Integer totalLosses = 0;

    @Column(name = "total_draws")
    @Builder.Default
    private Integer totalDraws = 0;
}
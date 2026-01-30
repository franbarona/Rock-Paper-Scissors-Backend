package com.rockpaperscissors.service;

import org.springframework.stereotype.Service;

import com.rockpaperscissors.dto.response.UserStatisticsResponse;
import com.rockpaperscissors.entity.User;
import com.rockpaperscissors.entity.UserStatistics;
import com.rockpaperscissors.exception.StatisticsNotFoundException;
import com.rockpaperscissors.exception.UserNotFoundException;
import com.rockpaperscissors.repository.UserRepository;
import com.rockpaperscissors.repository.UserStatisticsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {
    private final UserRepository userRepository;
    private final UserStatisticsRepository userStatisticsRepository;

    public UserStatisticsResponse getUserStatistics(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    return new UserNotFoundException(email);
                });

        UserStatistics stats = userStatisticsRepository.findByUserId(user.getId())
                .orElseThrow(() -> {
                    return new StatisticsNotFoundException(user.getId());
                });

        return UserStatisticsResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .totalMatches(stats.getTotalMatches())
                .totalWins(stats.getTotalWins())
                .totalLosses(stats.getTotalLosses())
                .totalDraws(stats.getTotalDraws())
                .build();
    }

    public void updateUserStatistics(User user, com.rockpaperscissors.enums.GameResult result) {
        UserStatistics stats = userStatisticsRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    return UserStatistics.builder()
                            .user(user)
                            .totalMatches(0)
                            .totalWins(0)
                            .totalLosses(0)
                            .totalDraws(0)
                            .build();
                });

        stats.setTotalMatches(stats.getTotalMatches() + 1);
        switch (result) {
            case WIN -> stats.setTotalWins(stats.getTotalWins() + 1);
            case LOSS -> stats.setTotalLosses(stats.getTotalLosses() + 1);
            case DRAW -> stats.setTotalDraws(stats.getTotalDraws() + 1);
        }

        userStatisticsRepository.save(stats);
    }

}

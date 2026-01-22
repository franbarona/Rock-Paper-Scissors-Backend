package com.rockpaperscissors.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.rockpaperscissors.dto.response.GamePlayResponse;
import com.rockpaperscissors.entity.GameMatch;
import com.rockpaperscissors.entity.User;
import com.rockpaperscissors.enums.GameResult;
import com.rockpaperscissors.exception.UserNotFoundException;
import com.rockpaperscissors.enums.GameMove;
import com.rockpaperscissors.repository.GameMatchRepository;
import com.rockpaperscissors.repository.UserRepository;
import com.rockpaperscissors.utils.RandomMoveSelector;

import lombok.RequiredArgsConstructor;

import com.rockpaperscissors.utils.GameRuleEngine;

@Service
@RequiredArgsConstructor
public class GameService {
    private final GameMatchRepository gameMatchRepository;
    private final UserRepository userRepository;

    public GamePlayResponse playMove(GameMove playerMove, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException(userEmail));

        GameMove computerMove = RandomMoveSelector.selectRandomMove();
        GameResult result = GameRuleEngine.determineWinner(playerMove, computerMove);

        GameMatch gameMatch = GameMatch.builder()
                .playerMove(playerMove)
                .computerMove(computerMove)
                .result(result)
                .user(user)
                .build();
        gameMatchRepository.save(gameMatch);
        return GamePlayResponse.builder()
                .id(gameMatch.getId())
                .playerMove(playerMove.getName())
                .computerMove(computerMove.getName())
                .result(result.getName())
                .build();
    }

    public List<GamePlayResponse> getHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException(userEmail));
        List<GameMatch> matches = gameMatchRepository.findByUserIdOrderByPlayedAtDesc(user.getId());
        return matches.stream().map(match -> GamePlayResponse.builder()
                .id(match.getId())
                .playerMove(match.getPlayerMove().getName())
                .computerMove(match.getComputerMove().getName())
                .result(match.getResult().getName())
                .build()).toList();
    }
}

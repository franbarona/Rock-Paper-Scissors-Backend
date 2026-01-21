package com.rockpaperscissors.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.rockpaperscissors.entity.GameResult;
import com.rockpaperscissors.entity.User;
import com.rockpaperscissors.model.GameMove;
import com.rockpaperscissors.repository.GameResultRepository;
import com.rockpaperscissors.repository.UserRepository;

@Service
public class GameService {
    private static final Random random = new Random();
    private final GameResultRepository gameResultRepository;
    private final UserRepository userRepository;

    public GameService(GameResultRepository gameResultRepository, UserRepository userRepository) {
        this.gameResultRepository = gameResultRepository;
        this.userRepository = userRepository;
    }

    public GameResult playMove(GameMove playerMove, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GameMove computerMove = getRandomMove();
        String result = determineWinner(playerMove, computerMove);

        GameResult gameResult = new GameResult(playerMove, computerMove, result, user);
        gameResultRepository.save(gameResult);
        return gameResult;
    }

    public List<GameResult> getHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("User not found"));
        return gameResultRepository.findByUserIdOrderByPlayedAtDesc(user.getId());
    }

    private GameMove getRandomMove() {
        GameMove[] moves = GameMove.values();
        return moves[random.nextInt(moves.length)];
    }

    private String determineWinner(GameMove player, GameMove computer) {
        if (player == computer)
            return "DRAW";

        return switch (player) {
            case ROCK -> computer == GameMove.SCISSORS ? "WIN" : "LOSE";
            case PAPER -> computer == GameMove.ROCK ? "WIN" : "LOSE";
            case SCISSORS -> computer == GameMove.PAPER ? "WIN" : "LOSE";
        };
    }
}

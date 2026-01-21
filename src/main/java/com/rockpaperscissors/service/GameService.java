package com.rockpaperscissors.service;

import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.rockpaperscissors.entity.GameResult;
import com.rockpaperscissors.model.GameMove;
import com.rockpaperscissors.repository.GameResultRepository;

@Service
public class GameService {
    private static final Random random = new Random();
    private final GameResultRepository gameResultRepository;

    public GameService(GameResultRepository gameResultRepository) {
        this.gameResultRepository = gameResultRepository;
    }

    public GameResult playMove(GameMove playerMove) {
        GameMove computerMove = getRandomMove();
        String result = determineWinner(playerMove, computerMove);

        GameResult gameResult = new GameResult(playerMove, computerMove, result);
        gameResultRepository.save(gameResult);

        return gameResult;
    }

    public List<GameResult> getHistory() {
        return gameResultRepository.findAllByOrderByPlayedAtDesc();
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

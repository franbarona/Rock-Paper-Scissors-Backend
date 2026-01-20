package com.rockpaperscissors.service;

import java.util.Random;

import org.springframework.stereotype.Service;

import com.rockpaperscissors.model.GameMove;
import com.rockpaperscissors.model.GameResult;

@Service
public class GameService {
    private static final Random random = new Random();

    public GameResult playMove(GameMove playerMove) {
        GameMove computerMove = getRandomMove();
        String result = determineWinner(playerMove, computerMove);

        return new GameResult(playerMove, computerMove, result);
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

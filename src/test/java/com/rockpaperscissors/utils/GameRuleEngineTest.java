package com.rockpaperscissors.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.rockpaperscissors.enums.GameMove;
import com.rockpaperscissors.enums.GameResult;

@DisplayName("GameRuleEngine Tests")
class GameRuleEngineTest {

    // ==================== DRAW SCENARIOS ====================

    @Test
    @DisplayName("Should return DRAW when both players play ROCK")
    void testDrawWhenBothPlayRock() {
        GameMove playerMove = GameMove.ROCK;
        GameMove computerMove = GameMove.ROCK;

        GameResult result = GameRuleEngine.determineWinner(playerMove, computerMove);

        assertEquals(GameResult.DRAW, result, "Result should be DRAW when both play ROCK");
    }

    // ==================== PLAYER WIN SCENARIOS ====================

    @Test
    @DisplayName("Should return WIN when player plays ROCK and computer plays SCISSORS")
    void testPlayerWinsWithRockAgainstScissors() {
        GameMove playerMove = GameMove.ROCK;
        GameMove computerMove = GameMove.SCISSORS;

        GameResult result = GameRuleEngine.determineWinner(playerMove, computerMove);

        assertEquals(GameResult.WIN, result, "Player should WIN: ROCK beats SCISSORS");
    }

    @Test
    @DisplayName("Should return WIN when player plays PAPER and computer plays ROCK")
    void testPlayerWinsWithPaperAgainstRock() {
        GameMove playerMove = GameMove.PAPER;
        GameMove computerMove = GameMove.ROCK;

        GameResult result = GameRuleEngine.determineWinner(playerMove, computerMove);

        assertEquals(GameResult.WIN, result, "Player should WIN: PAPER beats ROCK");
    }

    @Test
    @DisplayName("Should return WIN when player plays SCISSORS and computer plays PAPER")
    void testPlayerWinsWithScissorsAgainstPaper() {
        GameMove playerMove = GameMove.SCISSORS;
        GameMove computerMove = GameMove.PAPER;

        GameResult result = GameRuleEngine.determineWinner(playerMove, computerMove);

        assertEquals(GameResult.WIN, result, "Player should WIN: SCISSORS beats PAPER");
    }

    // ==================== PLAYER LOSS SCENARIOS ====================

    @Test
    @DisplayName("Should return LOSS when player plays ROCK and computer plays PAPER")
    void testPlayerLosesWithRockAgainstPaper() {
        GameMove playerMove = GameMove.ROCK;
        GameMove computerMove = GameMove.PAPER;

        GameResult result = GameRuleEngine.determineWinner(playerMove, computerMove);

        assertEquals(GameResult.LOSS, result, "Player should LOSS: PAPER beats ROCK");
    }

    @Test
    @DisplayName("Should return LOSS when player plays PAPER and computer plays SCISSORS")
    void testPlayerLosesWithPaperAgainstScissors() {
        GameMove playerMove = GameMove.PAPER;
        GameMove computerMove = GameMove.SCISSORS;

        GameResult result = GameRuleEngine.determineWinner(playerMove, computerMove);

        assertEquals(GameResult.LOSS, result, "Player should LOSS: SCISSORS beats PAPER");
    }

    @Test
    @DisplayName("Should return LOSS when player plays SCISSORS and computer plays ROCK")
    void testPlayerLosesWithScissorsAgainstRock() {
        GameMove playerMove = GameMove.SCISSORS;
        GameMove computerMove = GameMove.ROCK;

        GameResult result = GameRuleEngine.determineWinner(playerMove, computerMove);

        assertEquals(GameResult.LOSS, result, "Player should LOSS: ROCK beats SCISSORS");
    }

    // ==================== NULL HANDLING TESTS ====================

    @Test
    @DisplayName("Should throw NullPointerException when player move is null")
    void testNullPlayerMove() {
        GameMove playerMove = null;
        GameMove computerMove = GameMove.ROCK;

        assertThrows(NullPointerException.class, () -> GameRuleEngine.determineWinner(playerMove, computerMove),
            "Expected NullPointerException when player move is null");
    }
}
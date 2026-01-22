package com.rockpaperscissors.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.rockpaperscissors.enums.GameMove;

@DisplayName("RandomMoveSelector Tests")
class RandomMoveSelectorTest {

    @Test
    @DisplayName("Should return a non-null GameMove")
    void testSelectRandomMoveReturnsNotNull() {
        GameMove result = RandomMoveSelector.selectRandomMove();

        assertNotNull(result, "Random move should never be null");
    }

    @Test
    @DisplayName("Should return a valid GameMove enum value")
    void testSelectRandomMoveReturnsValidEnumValue() {
        GameMove result = RandomMoveSelector.selectRandomMove();

        assertTrue(isValidGameMove(result), "Random move should be a valid GameMove enum value");
    }

    // ==================== HELPER METHOD ====================

    private boolean isValidGameMove(GameMove move) {
        for (GameMove validMove : GameMove.values()) {
            if (move == validMove) {
                return true;
            }
        }
        return false;
    }
}
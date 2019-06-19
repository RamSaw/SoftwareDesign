package ru.hse.spb.model.engine.strategy

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.math.abs

class ConfusedStrategyTest : MobStrategyBaseTest() {

    @Test
    fun testMakeTurn() {
        val timeout = 1
        val strategy = ConfusedStrategy(timeout)
        val newPosition = strategy.makeTurn(mob, map)

        assertTrue(
            abs(mob.position.x - newPosition.x)
                    + abs(mob.position.y - newPosition.y) <= 1
        )
    }

    @Test
    fun testIsExpired() {
        val timeout = 5
        val strategy = ConfusedStrategy(timeout)

        for (i in 0 until timeout) {
            assertFalse(strategy.isExpired())
            strategy.makeTurn(mob, map)
        }

        assertTrue(strategy.isExpired())
    }
}
package ru.hse.spb.model.engine.strategy

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AggressiveStrategyTest : MobStrategyBaseTest() {

    @Test
    fun testMakeTurn() {
        val strategy = AggressiveStrategy(model)
        val newPosition = strategy.makeTurn(mob, map)

        assertTrue(dist(newPosition, player.position) < dist(mob.position, player.position))
    }

    @Test
    fun testIsExpired() {
        val strategy = AggressiveStrategy(model)

        assertFalse(strategy.isExpired())
    }
}
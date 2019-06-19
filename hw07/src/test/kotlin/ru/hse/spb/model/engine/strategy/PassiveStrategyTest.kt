package ru.hse.spb.model.engine.strategy

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PassiveStrategyTest : MobStrategyBaseTest() {

    @Test
    fun testMakeTurn() {
        val strategy = PassiveStrategy()
        val newPosition = strategy.makeTurn(mob, map)

        assertEquals(mob.position, newPosition)
    }

    @Test
    fun testIsExpired() {
        val strategy = PassiveStrategy()

        assertFalse(strategy.isExpired())
    }
}
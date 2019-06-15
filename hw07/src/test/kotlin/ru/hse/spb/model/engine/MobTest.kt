package ru.hse.spb.model.engine

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.hse.spb.model.Map
import ru.hse.spb.model.engine.strategy.ConfusedStrategy
import kotlin.math.abs

class MobTest {

    @Test
    fun testMovement() {
        val map = Map.generate()
        val start = map.getFreeCells()[0]
        val mob = Mob(1, start, ConfusedStrategy(100))
        mob.move(map)
        assertTrue(
            abs(mob.getCurrentPosition().x - start.x)
                    + abs(mob.getCurrentPosition().y - start.y) <= 1
        )
        assertTrue(map.getCell(mob.getCurrentPosition()) == Map.CellState.FREE)
    }
}
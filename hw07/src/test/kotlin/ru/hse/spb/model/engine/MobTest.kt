package ru.hse.spb.model.engine

import org.junit.Assert.assertTrue
import org.junit.Test
import ru.hse.spb.model.Map
import kotlin.math.abs

class MobTest {

    @Test
    fun testMovement() {
        val map = Map.generate()
        val start = map.getFreeCells()[0]
        val mob = DangerMob(1, start)
        mob.move(map)
        assertTrue(
            abs(mob.getCurrentPosition().x - start.x)
                    + abs(mob.getCurrentPosition().y - start.y) <= 1
        )
        assertTrue(map.getCell(mob.getCurrentPosition()) == Map.CellState.FREE)
    }
}
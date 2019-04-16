package ru.hse.spb.model.engine.strategy

import ru.hse.spb.model.Map
import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.Mob

/**
 * Confused strategy - mob walks randomly.
 */
class ConfusedStrategy(var timeout: Int) : MobStrategy {
    override fun makeTurn(mob: Mob, map: Map): Map.MapPosition {
        timeout--
        val position = mob.getCurrentPosition()
        val x = position.x
        val y = position.y
        return listOf(
            position,
            MapPosition(x + 1, y),
            MapPosition(x, y + 1),
            MapPosition(x - 1, y),
            MapPosition(x, y - 1)
        ).filter { map.getCell(it) != Map.CellState.WALL }.shuffled().first()
    }

    override fun isExpired(): Boolean {
        return timeout <= 0
    }
}

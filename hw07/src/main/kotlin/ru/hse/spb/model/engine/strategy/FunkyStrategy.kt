package ru.hse.spb.model.engine.strategy

import ru.hse.spb.model.Map
import ru.hse.spb.model.Model
import ru.hse.spb.model.engine.Mob

/**
 * Funky strategy - mob tries to get farther from the players.
 */
class FunkyStrategy(private val model: Model) : MobStrategy {
    override fun makeTurn(mob: Mob, map: Map): Map.MapPosition {
        val mobPosition = mob.getCurrentPosition()
        val playerPosition = model.players.getValue(model.getActivePlayer()).getCurrentPosition()

        val x = mobPosition.x + when {
            playerPosition.x > mobPosition.x -> -1
            playerPosition.x == mobPosition.x -> 0
            else -> 1
        }

        val y = mobPosition.y + when {
            playerPosition.y > mobPosition.y -> -1
            playerPosition.y == mobPosition.y -> 0
            else -> 1
        }

        if (map.getCell(Map.MapPosition(x, mobPosition.y)) != Map.CellState.WALL && x != mobPosition.x) {
            return Map.MapPosition(x, mobPosition.y)
        }

        if (map.getCell(Map.MapPosition(mobPosition.x, y)) != Map.CellState.WALL) {
            return Map.MapPosition(mobPosition.x, y)
        }

        return mobPosition
    }

    override fun isExpired(): Boolean {
        return false
    }
}

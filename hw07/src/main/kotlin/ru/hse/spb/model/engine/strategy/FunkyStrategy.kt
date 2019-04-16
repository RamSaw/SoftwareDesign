package ru.hse.spb.model.engine.strategy

import ru.hse.spb.model.Map
import ru.hse.spb.model.Model
import ru.hse.spb.model.engine.Mob

class FunkyStrategy(private val model: Model) : MobStrategy {
    override fun makeTurn(mob: Mob, map: Map): Map.MapPosition {
        val mobPosition = mob.getCurrentPosition()
        val playerPosition = model.player.getCurrentPosition()

        var x = mobPosition.x + when {
            playerPosition.x > mobPosition.x -> -1
            playerPosition.x == mobPosition.x -> 0
            else -> 1
        }

        var y = mobPosition.y + when {
            playerPosition.y > mobPosition.y -> -1
            playerPosition.y == mobPosition.y -> 0
            else -> 1
        }

        if (map.getCell(Map.MapPosition(x, y)) != Map.CellState.FREE) {
            if (map.getCell(Map.MapPosition(mobPosition.x, y)) != Map.CellState.FREE) {
                y = mobPosition.y
                if (map.getCell(Map.MapPosition(x, y)) != Map.CellState.FREE) {
                    x = mobPosition.x
                }
            } else {
                x = mobPosition.x
            }
        }

        return Map.MapPosition(x, y)
    }

    override fun isExpired(): Boolean {
        return false
    }
}

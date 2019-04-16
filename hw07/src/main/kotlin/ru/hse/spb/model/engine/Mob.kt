package ru.hse.spb.model.engine

import ru.hse.spb.model.Map
import ru.hse.spb.model.Map.CellState.FREE
import ru.hse.spb.model.Map.MapPosition

/**
 * This class represents mob(non playable characters).
 */
abstract class Mob(val type: MobType) : GameCharacter() {
    fun move(map: Map): MapPosition {
        val x = position.x
        val y = position.y
        return listOf(
            position,
            MapPosition(x + 1, y),
            MapPosition(x, y + 1),
            MapPosition(x - 1, y),
            MapPosition(x, y - 1)
        ).filter { map.getCell(it) == FREE }.shuffled().first()
    }

    override fun inclineDamage() = strength

    fun getConfused(confuseTime: Int) {
        TODO()
    }

    enum class MobType {
        DANGER,
        SWEET
    }
}

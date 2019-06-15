package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition
import java.io.Serializable


/**
 *  This class represents all game characters.
 */
abstract class GameCharacter : Serializable {
    protected abstract var health: Int
    protected abstract var level: Int
    protected abstract var strength: Int
    protected abstract var position: MapPosition

    fun getCurrentPosition() = position

    /**
     * Decrease character`s health for given amount.
     */
    abstract fun takeDamage(dmg: Int)

    abstract fun getCurrentLevel(): Int

    abstract fun getCurrentHealth(): Int

    /**
     * Return character`s strength to incline.
     */
    abstract fun inclineDamage(): Int

    /**
     * Change character`s position on the map.
     */
    fun changePosition(x: Int, y: Int) {
        position.x = x
        position.y = y
    }
}
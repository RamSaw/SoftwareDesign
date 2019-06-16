package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition
import java.io.Serializable


/**
 *  This class represents all game characters.
 */
abstract class GameCharacter : Serializable {
    abstract var health: Int
        protected set
    abstract var level: Int
        protected set
    protected abstract var strength: Int
    abstract var position: MapPosition
        protected set
    protected abstract var baseHealth: Int
    protected abstract var baseStrength: Int

    /**
     * Decrease character`s health for given amount.
     */
    abstract fun takeDamage(dmg: Int)

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
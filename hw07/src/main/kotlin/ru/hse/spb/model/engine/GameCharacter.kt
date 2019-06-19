package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition
import java.io.Serializable


/**
 *  This class represents all game characters.
 */
abstract class GameCharacter : Serializable {
    abstract val health: Int
    abstract var level: Int
        protected set
    protected abstract val strength: Int
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
     * Performs attack on other character.
     */
    open fun attack(other: GameCharacter) {
        other.takeDamage(this.inclineDamage())
    }

    /**
     * Change character`s position on the map.
     */
    fun changePosition(x: Int, y: Int) {
        position.x = x
        position.y = y
    }
}
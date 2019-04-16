package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition


/**
 *  This class represents all game characters.
 */
abstract class GameCharacter {
    protected abstract var health: Int
    protected abstract var level: Int
    protected abstract var strength: Int
    protected abstract var position: MapPosition

    fun getCurrentPosition() = position

    /**
     * Decrease character`s health for given amount.
     */
    fun takeDamage(dmg: Int) {
        health -= dmg
    }

    fun getCurrentLevel() = level

    fun getCurrentHealth() = health

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
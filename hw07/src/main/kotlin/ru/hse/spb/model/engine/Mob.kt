package ru.hse.spb.model.engine

import ru.hse.spb.model.Map
import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.strategy.MobStrategy

/**
 * This class represents mob(non playable characters).
 */
class Mob(
    override var level: Int,
    override var position: MapPosition,
    private var strategy: MobStrategy
) : GameCharacter() {
    private var temporaryStrategy: MobStrategy? = null

    override var health = DEFAULT_HEALTH + AMPLIFIER * level
    override var strength = DEFAULT_STRENGTH + AMPLIFIER * level

    companion object {
        private const val AMPLIFIER = 2
        private const val DEFAULT_HEALTH = 10
        private const val DEFAULT_STRENGTH = 1
    }

    /**
     * Makes a move corresponding to current strategy.
     */
    fun move(map: Map): MapPosition {
        if (temporaryStrategy != null && !temporaryStrategy!!.isExpired()) {
            return temporaryStrategy!!.makeTurn(this, map)
        }

        return strategy.makeTurn(this, map)
    }

    override fun inclineDamage() = strength

    override fun getCurrentLevel() = level

    override fun getCurrentHealth() = health

    override fun takeDamage(dmg: Int) {
        health -= dmg
    }

    /**
     * Set temporary strategy to a specified one.
     */
    fun setStrategy(strategy: MobStrategy) {
        temporaryStrategy = strategy
    }

    /**
     * Getter for current strategy.
     */
    fun getStrategy() = temporaryStrategy
}

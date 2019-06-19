package ru.hse.spb.model.engine

import ru.hse.spb.model.Map
import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.strategy.MobStrategy

/**
 * This class represents mob(non playable characters).
 */
class Mob(
    override val level: Int,
    override var position: MapPosition,
    private var strategy: MobStrategy
) : GameCharacter() {
    private var temporaryStrategy: MobStrategy? = null

    override var health = DEFAULT_HEALTH + AMPLIFIER * level
    override var strength = DEFAULT_STRENGTH + AMPLIFIER * level
    override var baseHealth: Int = 0
    override var baseStrength: Int = 0

    companion object {
        private const val AMPLIFIER = 2
        private const val DEFAULT_HEALTH = 10
        private const val DEFAULT_STRENGTH = 1
    }

    /**
     * Makes a move corresponding to current strategy.
     */
    fun move(map: Map): MapPosition {
        return if (temporaryStrategy?.isExpired() == true) {
            temporaryStrategy!!.makeTurn(this, map)
        } else {
            strategy.makeTurn(this, map)
        }
    }

    override fun inclineDamage() = strength

    override fun takeDamage(dmg: Int) {
        health -= dmg
    }

    /**
     * Set temporary strategy to a specified one.
     */
    fun setStrategy(strategy: MobStrategy) {
        temporaryStrategy = strategy
    }
}

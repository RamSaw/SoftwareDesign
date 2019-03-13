package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition

/**
 * This class represents mob with low attack.
 */
class SweetMob(
    override var level: Int,
    override var position: MapPosition
) : Mob(MobType.SWEET) {

    override var health = DEFAULT_HEALTH + AMPLIFIER * level
    override var strength = DEFAULT_STRENGTH + AMPLIFIER * level

    companion object {
        private const val AMPLIFIER = 1
        private const val DEFAULT_HEALTH = 5
        private const val DEFAULT_STRENGTH = 0
    }
}
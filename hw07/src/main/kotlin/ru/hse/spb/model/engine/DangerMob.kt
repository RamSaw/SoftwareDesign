package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition

class DangerMob(
    override var level: Int,
    override var position: MapPosition
) : Mob() {

    override var health = DEFAULT_HEALTH + AMPLIFIER * level
    override var strength = DEFAULT_STRENGTH + AMPLIFIER * level

    companion object {
        private const val AMPLIFIER = 2
        private const val DEFAULT_HEALTH = 10
        private const val DEFAULT_STRENGTH = 1
    }
}
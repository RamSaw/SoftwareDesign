package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition

/**
 * This class represents basic player.
 */
class Player(loc: MapPosition) : BasePlayer(loc) {

    override fun inclineDamage() = strength

    override fun levelUp() {
        level++
        health += AMPLIFIER
        strength += AMPLIFIER
    }

    override fun takeOffEquipment() {
        if (equipment.isOnCharacter) {
            health -= equipment.additionalHealth
            strength -= equipment.additionalStrength
            equipment.takeOff()
        }
    }

    override fun takeOnEquipment() {
        if (!equipment.isOnCharacter) {
            health += equipment.additionalHealth
            strength += equipment.additionalStrength
            equipment.takeOn()
        }
    }
}
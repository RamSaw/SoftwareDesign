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

    override fun takeOnOffEquipment(equipmentId: Int) {
        if (equipmentId >= equipment.size || equipmentId < 0) {
            return
        }

        val eq = equipment[equipmentId]

        if (eq.isOnCharacter) {
            health -= eq.additionalHealth
            strength -= eq.additionalStrength
            eq.takeOff()
        } else {
            health += eq.additionalHealth
            strength += eq.additionalStrength
            eq.takeOn()
        }
    }

    override fun takeDamage(dmg: Int) {
        health -= dmg
    }

    override fun getCurrentLevel() = level

    override fun getCurrentHealth() = health
}
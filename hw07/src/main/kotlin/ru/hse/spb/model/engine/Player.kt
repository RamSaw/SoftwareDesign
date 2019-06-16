package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition
import java.lang.Integer.max

/**
 * This class represents basic player.
 */
class Player(loc: MapPosition) : BasePlayer(loc) {

    override fun inclineDamage() = strength

    override fun levelUp() {
        level++
        baseHealth += AMPLIFIER
        baseStrength += AMPLIFIER
    }

    override fun takeOnOffEquipment(equipmentId: Int) {
        if (equipmentId >= equipment.size || equipmentId < 0) {
            return
        }

        val eq = equipment[equipmentId]

        if (eq.isOnCharacter) {
            if (health > eq.additionalHealth) {
                eq.takeOff()
            }
        } else {
            eq.takeOn()
        }
    }

    override fun takeDamage(dmg: Int) {
        baseHealth -= dmg
    }
}
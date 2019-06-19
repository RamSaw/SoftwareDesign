package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition

/**
 * This class represents basic player.
 */
class Player(loc: MapPosition) : BasePlayer(loc) {
    override var level = super.level
    override val equipment = mutableListOf<Equipment>()

    override fun inclineDamage() = strength

    override fun levelUp() {
        level++
        baseHealth += AMPLIFIER
        baseStrength += AMPLIFIER
        addRandomEquipment()
    }

    override fun takeOnOffEquipment(equipmentId: Int) {
        if (equipmentId >= equipment.size || equipmentId < 0) {
            return
        }

        val eq = equipment[equipmentId]

        if (eq.isOnCharacter()) {
            if (health > eq.additionalHealth) {
                eq.takeOff()
            }
        } else {
            if (getEquipmentOn().isEmpty()) {
                eq.takeOn()
            }
        }
    }

    override fun takeDamage(dmg: Int) {
        baseHealth -= dmg
    }

    private fun addRandomEquipment() {
        val equipmentSize = equipment.size

        if (equipmentSize == MAX_EQUIPMENT_CNT) {
            return
        }

        val newEquipment = Equipment(
            PLAYER_EQUIPMENT_NAME + equipmentSize.toString(),
            false,
            (1..EQUIPMENT_MAX_HEALTH).random(),
            (1..EQUIPMENT_MAX_STRENGTH).random())

        equipment.add(newEquipment)
    }

    companion object {
        private const val PLAYER_EQUIPMENT_NAME = "PLAYER_EQUIPMENT"
        private const val EQUIPMENT_MAX_HEALTH = 10
        private const val EQUIPMENT_MAX_STRENGTH = 2
    }
}
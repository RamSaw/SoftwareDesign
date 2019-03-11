package ru.hse.spb.model.enigne

import ru.hse.spb.model.Map.MapPosition

class Player(loc: MapPosition) : GameCharacter() {
    override var level: Int = 1
    override var position: MapPosition = loc
    private val equipment = Equipment(EQUIPMENT_NAME, true, DEFAULT_HEALTH, DEFAULT_STRENGTH)
    override var health: Int = DEFAULT_HEALTH + equipment.additionalHealth
    override var strength: Int = DEFAULT_STRENGTH + equipment.additionalStrength

    fun levelUp() {
        level++
        health += AMPLIFIER
        strength += AMPLIFIER
    }

    fun takeOffEquipment() {
        if (equipment.isOnCharacter) {
            health -= equipment.additionalHealth
            strength -= equipment.additionalStrength
            equipment.takeOff()
        }
    }

    fun takeOnEquipment() {
        if (!equipment.isOnCharacter) {
            health += equipment.additionalHealth
            strength += equipment.additionalStrength
            equipment.takeOn()
        }
    }

    companion object {
        private const val EQUIPMENT_NAME = "GODSWORD"
        private const val AMPLIFIER = 3
        private const val DEFAULT_HEALTH = 10
        private const val DEFAULT_STRENGTH = 2
    }
}
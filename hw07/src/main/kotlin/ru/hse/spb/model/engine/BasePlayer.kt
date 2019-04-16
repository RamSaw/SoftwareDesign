package ru.hse.spb.model.engine

import ru.hse.spb.model.Map
import ru.hse.spb.model.Map.MapPosition

abstract class BasePlayer(loc: MapPosition) : GameCharacter() {
    override var level: Int = 1
    override var position: Map.MapPosition = loc
    protected val equipment = Equipment(EQUIPMENT_NAME, true, DEFAULT_HEALTH, DEFAULT_STRENGTH)
    override var health: Int = DEFAULT_HEALTH + equipment.additionalHealth
    override var strength: Int = DEFAULT_STRENGTH + equipment.additionalStrength
    protected val AMPLIFIER = 3


    /**
     * Increases health and strength.
     */
    abstract fun levelUp()

    abstract fun takeOffEquipment()

    abstract fun takeOnEquipment()

    fun getEquipmentName(): String = equipment.name

    companion object {
        private const val EQUIPMENT_NAME = "GODSWORD"
        private const val DEFAULT_HEALTH = 10
        private const val DEFAULT_STRENGTH = 2
    }
}
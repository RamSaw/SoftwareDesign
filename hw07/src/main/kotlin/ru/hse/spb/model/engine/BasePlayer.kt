package ru.hse.spb.model.engine

import ru.hse.spb.model.Map
import ru.hse.spb.model.Map.MapPosition

/**
 * This class represents common attributes for all type of players
 */
abstract class BasePlayer(loc: MapPosition) : GameCharacter() {
    override var level: Int = 1
    override var position: Map.MapPosition = loc
    protected val equipment = arrayOf(Equipment(EQUIPMENT_NAME, true, DEFAULT_HEALTH, DEFAULT_STRENGTH))
    override var health: Int = DEFAULT_HEALTH + equipment.filter { it.isOnCharacter }.map { it.additionalHealth }.sum()
    override var strength: Int = DEFAULT_STRENGTH + equipment.filter { it.isOnCharacter }.map { it.additionalStrength }.sum()
    protected val AMPLIFIER = 3


    /**
     * Increases health and strength.
     */
    abstract fun levelUp()

    /**
     * Takes on equipment if it is off and vice versa.
     */
    abstract fun takeOnOffEquipment(equipmentId: Int)

    /**
     * Getter for all available equipment.
     */
    fun getEquipmentNames(): List<String> = equipment.map { it.name }

    companion object {
        private const val EQUIPMENT_NAME = "GODSWORD"
        private const val DEFAULT_HEALTH = 10
        private const val DEFAULT_STRENGTH = 2
    }
}
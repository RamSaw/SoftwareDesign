package ru.hse.spb.model.engine

import ru.hse.spb.model.Map
import ru.hse.spb.model.Map.MapPosition

/**
 * This class represents common attributes for all type of players
 */
abstract class BasePlayer(loc: MapPosition) : GameCharacter() {
    override var level: Int = 1
    override var position: MapPosition = loc
    abstract val equipment: List<Equipment>

    override var health: Int
        get() = baseHealth + getAdditionalHealth()
        set(value) {}
    override var strength: Int
        get() = baseStrength + getAdditionalStrength()
        set(value) {}
    protected val AMPLIFIER = 3
    protected val MAX_EQUIPMENT_CNT = 10

    override var baseHealth: Int = DEFAULT_HEALTH
    override var baseStrength: Int = DEFAULT_STRENGTH

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

    private fun getAdditionalHealth(): Int = equipment.filter { it.isOnCharacter() }.map { it.additionalHealth }.sum()
    private fun getAdditionalStrength(): Int = equipment.filter { it.isOnCharacter() }.map { it.additionalStrength }.sum()
    protected fun getEquipmentOn(): List<Equipment> = equipment.filter { it.isOnCharacter() }

    companion object {
        private const val DEFAULT_HEALTH = 20
        private const val DEFAULT_STRENGTH = 4
    }
}
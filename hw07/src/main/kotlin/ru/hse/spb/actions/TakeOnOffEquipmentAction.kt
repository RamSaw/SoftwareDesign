package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * This action takes equipment on if it is not and vice versa.
 */
class TakeOnOffEquipmentAction(private val equipmentId: Int) : Action {
    override fun execute(model: Model) {
        model.takeOnOffPlayerEquipment(equipmentId)
    }
}

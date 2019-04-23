package ru.hse.spb.actions

import ru.hse.spb.model.Model

class TakeOnOffEquipmentAction(private val equipmentId: Int) : Action {
    override fun execute(model: Model) {
        model.takeOnOffPlayerEquipment(equipmentId)
    }
}

package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Provides action for given digit.
 */
class DigitActionProvider(private val model: Model) {
    fun actionFor(digit: Int): Action = TakeOnOffEquipmentAction(model, digit)
}

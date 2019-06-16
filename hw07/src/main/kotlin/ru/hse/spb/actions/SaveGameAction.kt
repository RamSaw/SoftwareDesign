package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents save game action.
 */
object SaveGameAction : Action {
    override fun execute(model: Model) {
        model.save()
    }
}

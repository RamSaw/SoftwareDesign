package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents save game action.
 */
class SaveGameAction(private val model: Model) : Action {
    override fun execute() {
        model.save()
    }
}

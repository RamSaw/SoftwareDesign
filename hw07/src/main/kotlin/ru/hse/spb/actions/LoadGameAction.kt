package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents load action.
 */
class LoadGameAction(private val model: Model) : Action {
    override fun execute() {
        model.load()
    }
}

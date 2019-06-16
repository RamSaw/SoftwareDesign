package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents load action.
 */
object LoadGameAction : Action {
    override fun execute(model: Model) {
        model.load()
    }
}

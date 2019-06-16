package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents quit game action.
 */
object QuitGameAction : Action {
    override fun execute(model: Model) {
        model.finishGame()
    }
}

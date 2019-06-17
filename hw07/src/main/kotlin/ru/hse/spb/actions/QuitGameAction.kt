package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents quit game action.
 */
class QuitGameAction(private val model: Model) : Action {
    override fun execute() {
        model.finishGame()
    }
}

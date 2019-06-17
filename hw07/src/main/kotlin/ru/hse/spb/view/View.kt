package ru.hse.spb.view

import ru.hse.spb.actions.Action
import ru.hse.spb.model.Model

/**
 * This interface represents view for the game.
 */
interface View {
    /**
     * Draws model current state.
     */
    fun draw(model: Model?)

    /**
     * Waits for user to act.
     */
    fun waitAction()

    /**
     * Closes view session.
     */
    fun stop()
}

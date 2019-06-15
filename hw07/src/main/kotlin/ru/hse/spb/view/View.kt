package ru.hse.spb.view

import ru.hse.spb.controller.Controller
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
     * Reads user action.
     */
    fun getAction(): Controller.Companion.PlayerAction

    /**
     * Closes view session.
     */
    fun stop()
}

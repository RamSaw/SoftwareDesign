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
    fun draw(model: Model)

    /**
     * Reads user action.
     */
    fun getAction(): Action

    /**
     * Closes view session.
     */
    fun stop()

    /**
     * Returns player id.
     */
    fun getPlayerId(): Int
}

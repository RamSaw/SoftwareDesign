package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents user action.
 */
interface Action {
    /**
     * Applies action to the model.
     */
    fun execute(model: Model)
}

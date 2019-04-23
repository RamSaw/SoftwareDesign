package ru.hse.spb.controller

import ru.hse.spb.model.Model
import ru.hse.spb.view.View

/**
 * This class interacts with user and provides actions to model.
 */
class Controller(private val model: Model,
                 private val view: View) {

    /**
     * Main game loop.
     */
    fun run() {
        while (true) {
            val action = view.getAction()
            action.execute(model)

            if (model.isGameFinished()) {
                break
            }
        }
    }
}
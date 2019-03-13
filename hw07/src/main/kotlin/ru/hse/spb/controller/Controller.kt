package ru.hse.spb.controller

import ru.hse.spb.model.Model
import ru.hse.spb.view.View

/**
 * This class interacts with user and provides actions to model.
 */
class Controller(private val model: Model,
                 private val view: View) {

    fun run() {
        while (true) {
            val action = view.getAction()

            if (action == PlayerAction.QUIT) {
                break
            }

            model.move(action)
        }
    }

    companion object {
        enum class PlayerAction {
            MOVE_UP,
            MOVE_DOWN,
            MOVE_LEFT,
            MOVE_RIGHT,
            TAKE_OFF_EQUIPMENT,
            TAKE_ON_EQUIPMENT,
            QUIT,
            UNKNOWN
        }
    }
}
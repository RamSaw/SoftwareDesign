package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents moving right action.
 */
class MoveRightAction(private val model: Model) : Action {
    override fun execute() {
        model.movePlayer(Model.PlayerMove.MOVE_RIGHT)
    }
}

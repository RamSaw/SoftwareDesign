package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents moving right action.
 */
object MoveRightAction : Action {
    override fun execute(model: Model) {
        model.movePlayer(Model.PlayerMove.MOVE_RIGHT)
    }
}

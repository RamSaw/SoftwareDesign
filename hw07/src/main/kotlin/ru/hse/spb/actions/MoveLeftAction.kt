package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents moving left action.
 */
object MoveLeftAction : Action {
    override fun execute(model: Model) {
        model.movePlayer(Model.PlayerMove.MOVE_LEFT)
    }
}

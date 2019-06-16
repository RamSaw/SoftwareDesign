package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents moving down action.
 */
object MoveDownAction : Action {
    override fun execute(model: Model) {
        model.movePlayer(Model.PlayerMove.MOVE_DOWN)
    }
}

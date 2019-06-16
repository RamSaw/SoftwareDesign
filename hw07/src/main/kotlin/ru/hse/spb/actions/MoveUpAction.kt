package ru.hse.spb.actions

import ru.hse.spb.model.Model

/**
 * Represents moving up action.
 */
object MoveUpAction : Action {
    override fun execute(model: Model) {
        model.movePlayer(Model.PlayerMove.MOVE_UP)
    }
}

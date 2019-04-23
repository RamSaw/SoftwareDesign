package ru.hse.spb.actions

import ru.hse.spb.model.Model

object MoveUpAction : Action {
    override fun execute(model: Model) {
        model.movePlayer(Model.PlayerMove.MOVE_UP)
    }
}

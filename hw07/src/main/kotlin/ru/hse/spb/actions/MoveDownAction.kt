package ru.hse.spb.actions

import ru.hse.spb.model.Model

object MoveDownAction : Action {
    override fun execute(model: Model) {
        model.movePlayer(Model.PlayerMove.MOVE_DOWN)
    }
}

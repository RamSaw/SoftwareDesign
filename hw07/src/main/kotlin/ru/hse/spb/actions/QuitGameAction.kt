package ru.hse.spb.actions

import ru.hse.spb.model.Model

object QuitGameAction : Action {
    override fun execute(model: Model) {
        model.finishGame()
    }
}

package ru.hse.spb

import ru.hse.spb.controller.Controller
import ru.hse.spb.model.Map
import ru.hse.spb.model.WorldModel
import ru.hse.spb.view.ConsoleView

object RoguelikeSinglePlayerApplication {
    fun start() {
        val map = Map.generate()
        val model = WorldModel(map)
        val playerId = model.addPlayer()
        val view = ConsoleView(playerId)
        val controller = Controller(model, view)

        controller.run()
        view.stop()
    }
}
package ru.hse.spb

import ru.hse.spb.controller.Controller
import ru.hse.spb.model.Map
import ru.hse.spb.model.WorldModel
import ru.hse.spb.view.ConsoleView

fun main() {
    val map = Map.generate()
    val model = WorldModel(map)
    val view = ConsoleView
    val controller = Controller(model, view)

    controller.run()
}

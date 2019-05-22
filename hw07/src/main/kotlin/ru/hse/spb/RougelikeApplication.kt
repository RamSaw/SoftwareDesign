package ru.hse.spb

import ru.hse.spb.controller.Controller
import ru.hse.spb.model.Map
import ru.hse.spb.model.WorldModel
import ru.hse.spb.view.ConsoleView

fun main(args: Array<String>) {
    val map = if (args.isEmpty()) Map.generate() else Map.load(args[0])
    val model = WorldModel(map)
    val view = ConsoleView
    val controller = Controller(model, view)

    controller.run()
    view.stop()
}

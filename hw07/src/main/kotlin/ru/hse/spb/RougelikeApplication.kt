package ru.hse.spb

import ru.hse.spb.controller.Controller
import ru.hse.spb.model.Map
import ru.hse.spb.model.WorldModel
import ru.hse.spb.view.ConsoleView
import java.lang.IllegalArgumentException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val view = ConsoleView
    val map = when (args.size) {
        0 -> Map.generate()
        1 -> Map.load(args[0])
        2 -> {
            val width = args[0].toInt()
            val height = args[1].toInt()
            if (width < 3 || height < 3) {
                println("Width and height must be at least 3")
                printHelp()
                exitProcess(1)
            }
            Map.generate(width, height)
        }
        else -> {
            println("More then 2 arguments is not supported!")
            printHelp()
            exitProcess(1)
        }
    }
    val model = WorldModel(map, view)
    val controller = Controller(model, view)

    controller.run()
    view.stop()
}

fun printHelp() {
    print("Params: None or <path to file with custom map> or <width> <height>")
}
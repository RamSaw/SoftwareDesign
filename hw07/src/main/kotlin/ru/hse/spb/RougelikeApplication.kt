package ru.hse.spb

import ru.hse.spb.actions.*
import ru.hse.spb.controller.Controller
import ru.hse.spb.model.FailedLoadException
import ru.hse.spb.model.Map
import ru.hse.spb.model.MapFormatException
import ru.hse.spb.model.WorldModel
import ru.hse.spb.view.ConsoleView
import java.io.FileNotFoundException
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val view = ConsoleView
    val map = when (args.size) {
        0 -> Map.generate()
        1 -> {
            try {
                Map.load(args[0])
            } catch (e: FileNotFoundException) {
                exitWithError("File not found. Error message: " + e.message)
            } catch (e: MapFormatException) {
                exitWithError("Wrong file format. Error message: " + e.message)
            }
        }
        2 -> {
            try {
                val width = args[0].toInt()
                val height = args[1].toInt()

                if (width < 3 || height < 3) {
                    exitWithError("Width and height must be at least 3")
                }
                Map.generate(width, height)
            } catch (e: NumberFormatException) {
                exitWithError("You've passed not integers. Error message: " + e.message)
            }
        }
        else -> exitWithError("More then 2 arguments is not supported!")
    }
    val model = WorldModel(map as Map, view)
    val controller = Controller(model, view)

    view.moveDownAction = MoveDownAction(model)
    view.moveUpAction = MoveUpAction(model)
    view.moveLeftAction = MoveLeftAction(model)
    view.moveRightAction = MoveRightAction(model)
    view.quitGameAction = QuitGameAction(model)
    view.saveGameAction = SaveGameAction(model)
    view.loadGameAction = LoadGameAction(model)
    view.digitActionProvider = DigitActionProvider(model)

    try {
        controller.run()
        view.stop()
    } catch (e: FailedLoadException) {
        e.message?.let { exitWithError(it) }
    }
}

fun exitWithError(errorMessage: String) {
    System.out.println(errorMessage)
    printHelp()
    exitProcess(1)
}

fun printHelp() {
    print("Params: None or <path to file with custom map> or <width> <height>")
}
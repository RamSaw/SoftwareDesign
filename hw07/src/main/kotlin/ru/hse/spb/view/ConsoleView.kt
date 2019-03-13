package ru.hse.spb.view

import com.googlecode.lanterna.TerminalFacade
import com.googlecode.lanterna.input.Key
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.terminal.Terminal
import ru.hse.spb.controller.Controller
import ru.hse.spb.model.Map
import ru.hse.spb.model.Model

object ConsoleView: View {
    private const val MAP_POSITION_X = 0
    private const val MAP_POSITION_Y = 0

    private val screen = Screen(TerminalFacade.createTerminal())

    init {
        screen.startScreen()
        screen.cursorPosition = null
    }

    override fun getAction(): Controller.Companion.PlayerAction {
        var action = Controller.Companion.PlayerAction.UNKNOWN

        while (action == Controller.Companion.PlayerAction.UNKNOWN) {
            val key = screen.readInput() ?: continue

            action = when (key.kind) {
                Key.Kind.ArrowDown -> Controller.Companion.PlayerAction.MOVE_UP
                Key.Kind.ArrowUp -> Controller.Companion.PlayerAction.MOVE_DOWN
                Key.Kind.ArrowLeft -> Controller.Companion.PlayerAction.MOVE_LEFT
                Key.Kind.ArrowRight -> Controller.Companion.PlayerAction.MOVE_RIGHT
                Key.Kind.NormalKey -> when (key.character) {
                    'b' -> Controller.Companion.PlayerAction.TAKE_ON_EQUIPMENT
                    'n' -> Controller.Companion.PlayerAction.TAKE_OFF_EQUIPMENT
                    'q' -> Controller.Companion.PlayerAction.QUIT
                    else -> Controller.Companion.PlayerAction.UNKNOWN
                }
                else -> Controller.Companion.PlayerAction.UNKNOWN
            }
        }

        return action
    }

    override fun draw(model: Model) {
        drawMap(model)
        drawPlayer(model)
        drawMobs(model)
        drawInfoPanel(model)

        screen.refresh()
    }

    private fun drawMap(model: Model) {
        val map = model.map
        val field = map.field

        for ((i, row) in field.withIndex()) {
            for (j in row.indices) {
                screen.putString(
                    MAP_POSITION_X + j,
                    MAP_POSITION_Y + i,
                    cellStateToString(row[j]),
                    Terminal.Color.WHITE,
                    Terminal.Color.BLACK)
            }
        }
    }

    private fun drawPlayer(model: Model) {
        val playerPosition = model.player.getCurrentPosition()
        screen.putString(
            MAP_POSITION_X + playerPosition.x,
            MAP_POSITION_Y + playerPosition.y,
            "p",
            Terminal.Color.WHITE,
            Terminal.Color.BLACK)
    }

    private fun drawMobs(model: Model) {
        for (mob in model.mobs) {
            val mobPosition = mob.getCurrentPosition()
            screen.putString(
                MAP_POSITION_X + mobPosition.x,
                MAP_POSITION_Y + mobPosition.y,
                "m",
                Terminal.Color.WHITE,
                Terminal.Color.BLACK
            )
        }
    }

    private fun drawInfoPanel(model: Model) {
        val player = model.player
    }

    private fun cellStateToString(state: Map.CellState): String {
        return when (state) {
            Map.CellState.FREE -> "."
            Map.CellState.WALL -> "#"
        }
    }
}

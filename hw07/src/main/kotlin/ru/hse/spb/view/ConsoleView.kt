package ru.hse.spb.view

import com.googlecode.lanterna.TerminalFacade
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.terminal.Terminal
import ru.hse.spb.model.Map
import ru.hse.spb.model.Model

object ConsoleView: View {
    private const val MAP_POSITION_X = 0
    private const val MAP_POSITION_Y = 0

    private val screen = Screen(TerminalFacade.createTerminal())

    init {
        screen.startScreen()
    }

    override fun draw(model: Model) {
        drawMap(model)
        drawPlayer(model)
        drawDangerMobs(model)
        drawSweetMobs(model)
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
        val playerPosition = model.player.position
        screen.putString(
            MAP_POSITION_X + playerPosition.x,
            MAP_POSITION_Y + playerPosition.y,
            "p",
            Terminal.Color.WHITE,
            Terminal.Color.BLACK)
    }

    private fun drawInfoPanel(model: Model) {
        val player = model.player

        screen.putString(
            MAP_POSITION_X + playerPosition.x,
            MAP_POSITION_Y + playerPosition.y,
            "p",
            Terminal.Color.WHITE,
            Terminal.Color.BLACK)

    }

    private fun cellStateToString(state: Map.CellState): String {
        return when (state) {
            Map.CellState.FREE -> "."
            Map.CellState.WALL -> "#"
        }
    }
}

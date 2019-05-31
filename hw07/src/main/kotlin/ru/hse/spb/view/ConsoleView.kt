package ru.hse.spb.view

import com.googlecode.lanterna.TerminalFacade
import com.googlecode.lanterna.input.Key
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.terminal.Terminal
import ru.hse.spb.actions.*
import ru.hse.spb.model.Map
import ru.hse.spb.model.Model
import ru.hse.spb.model.engine.BasePlayer
import java.io.Serializable
import java.lang.Integer.max

/**
 * Simple view implementation, uses console graphics.
 */
class ConsoleView(private val playerId: Int) : View, Serializable {
    override fun getPlayerId(): Int = playerId

    companion object {
        private const val MAP_POSITION_X = 0
        private const val MAP_POSITION_Y = 0
        private const val INFO_PADDING_X = 10
        private const val INFO_PADDING_Y = 0
    }

    private val controlInfo = mutableListOf(
        "quit: q",
        "save: s",
        "load: l (works only before the first move)",
        "move: arrows",
        "take equipment on/off: corresponding number")

    private val screen = Screen(TerminalFacade.createTerminal())

    init {
        controlInfo.add("your player id: $playerId")
        screen.startScreen()
        screen.cursorPosition = null
    }

    override fun stop() {
        screen.stopScreen()
    }

    override fun getAction(model: Model): Action? {
        var action : Action? = null

        while (model.getActivePlayer() == playerId) {
            val key = screen.readInput() ?: continue

            action = when (key.kind) {
                Key.Kind.ArrowDown -> MoveDownAction
                Key.Kind.ArrowUp -> MoveUpAction
                Key.Kind.ArrowLeft -> MoveLeftAction
                Key.Kind.ArrowRight -> MoveRightAction
                Key.Kind.NormalKey -> when (key.character) {
                    'q' -> QuitGameAction
                    's' -> SaveGameAction
                    'l' -> LoadGameAction
                    in '0'..'9' -> TakeOnOffEquipmentAction(key.character.toInt() - '0'.toInt())
                    else -> null
                }
                else -> null
            }

            if (action != null) {
                break
            }
        }

        return action
    }

    override fun draw(model: Model) {
        drawMap(model)
        drawMobs(model)
        drawPlayer(model)
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
                    Terminal.Color.BLACK
                )
            }
        }
    }

    private fun drawPlayer(model: Model) {
        drawPlayer(model.players.getValue(model.getActivePlayer()), Terminal.Color.BLUE)
        drawPlayer(model.players.getValue(playerId), Terminal.Color.GREEN)
        model.players.filterKeys { i -> i != playerId && i != model.getActivePlayer() }
            .values.forEach { drawPlayer(it, Terminal.Color.YELLOW) }
    }

    private fun drawPlayer(player: BasePlayer, color: Terminal.Color) {
        val playerPosition = player.getCurrentPosition()
        screen.putString(
            MAP_POSITION_X + playerPosition.x,
            MAP_POSITION_Y + playerPosition.y,
            "p",
            color,
            Terminal.Color.BLACK
        )
    }

    private fun drawMobs(model: Model) {
        for (mob in model.mobs) {
            val mobPosition = mob.getCurrentPosition()
            screen.putString(
                MAP_POSITION_X + mobPosition.x,
                MAP_POSITION_Y + mobPosition.y,
                "m",
                Terminal.Color.RED,
                Terminal.Color.BLACK
            )
        }
    }

    private fun drawInfoPanel(model: Model) {
        val map = model.map
        val player = model.players.getValue(playerId)

        screen.putString(
            MAP_POSITION_X + map.getWidth() + INFO_PADDING_X,
            MAP_POSITION_Y + INFO_PADDING_Y,
            "level: " + player.getCurrentLevel(),
            Terminal.Color.WHITE,
            Terminal.Color.BLACK
        )
        screen.putString(
            MAP_POSITION_X + map.getWidth() + INFO_PADDING_X,
            MAP_POSITION_Y + INFO_PADDING_Y + 1,
            "equipment: " + player.getEquipmentNames().joinToString(),
            Terminal.Color.WHITE,
            Terminal.Color.BLACK
        )
        screen.putString(
            MAP_POSITION_X + map.getWidth() + INFO_PADDING_X,
            MAP_POSITION_Y + INFO_PADDING_Y + 2,
            "strength: " + player.inclineDamage(),
            Terminal.Color.WHITE,
            Terminal.Color.BLACK
        )

        val health = max(player.getCurrentHealth(), 0)

        screen.putString(
            MAP_POSITION_X + map.getWidth() + INFO_PADDING_X,
            MAP_POSITION_Y + INFO_PADDING_Y + 3,
            "health:          ",
            Terminal.Color.WHITE,
            Terminal.Color.BLACK
        )
        screen.putString(
            MAP_POSITION_X + map.getWidth() + INFO_PADDING_X,
            MAP_POSITION_Y + INFO_PADDING_Y + 3,
            "health: $health",
            Terminal.Color.WHITE,
            Terminal.Color.BLACK
        )

        if (health == 0) {
            screen.putString(
                MAP_POSITION_X + map.getWidth() + INFO_PADDING_X,
                MAP_POSITION_Y + INFO_PADDING_Y + 4,
                "GAME OVER",
                Terminal.Color.WHITE,
                Terminal.Color.BLACK
            )
        }

        drawControlInfo(MAP_POSITION_X + map.getWidth() + INFO_PADDING_X,
                      MAP_POSITION_Y + INFO_PADDING_Y + 5)
        screen.putString(
            MAP_POSITION_X + map.getWidth() + INFO_PADDING_X,
            MAP_POSITION_Y + INFO_PADDING_Y + 5 + controlInfo.size,
            "active player id: ${model.getActivePlayer()}",
            Terminal.Color.WHITE,
            Terminal.Color.BLACK
        )
    }

    private fun drawControlInfo(x: Int, y: Int) {
        for ((i, message) in controlInfo.withIndex()) {
            screen.putString(
                x,
                y + i,
                message,
                Terminal.Color.WHITE,
                Terminal.Color.BLACK
            )
        }
    }

    private fun cellStateToString(state: Map.CellState): String {
        return when (state) {
            Map.CellState.FREE -> " "
            Map.CellState.WALL, Map.CellState.OCCUPIED -> "#"
        }
    }
}

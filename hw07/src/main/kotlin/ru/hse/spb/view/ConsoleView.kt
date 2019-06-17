package ru.hse.spb.view

import com.googlecode.lanterna.TerminalFacade
import com.googlecode.lanterna.input.Key
import com.googlecode.lanterna.screen.Screen
import com.googlecode.lanterna.terminal.Terminal
import ru.hse.spb.actions.*
import ru.hse.spb.model.Map
import ru.hse.spb.model.Model
import java.io.Serializable
import java.lang.Integer.max

/**
 * Simple view implementation, uses console graphics.
 */
object ConsoleView : View, Serializable {
    var moveDownAction: Action? = null
    var moveUpAction: Action? = null
    var moveLeftAction: Action? = null
    var moveRightAction: Action? = null
    var quitGameAction: Action? = null
    var saveGameAction: Action? = null
    var loadGameAction: Action? = null
    var digitActionProvider: DigitActionProvider? = null

    private const val MAP_POSITION_X = 0
    private const val MAP_POSITION_Y = 0
    private const val INFO_PADDING_X = 10
    private const val INFO_PADDING_Y = 0

    private val CONTROL_INFO = arrayOf(
        "quit: q",
        "save: s",
        "load: l (works only before the first move)",
        "move: arrows",
        "take equipment on/off: corresponding number")

    private val screen = Screen(TerminalFacade.createTerminal())
    private var currentModel: Model? = null

    init {
        screen.terminal.addResizeListener(Terminal.ResizeListener { draw(currentModel) })
        screen.startScreen()
        screen.cursorPosition = null
    }

    override fun stop() {
        screen.stopScreen()
    }

    override fun waitAction() {
        var action : Action?

        while (true) {
            val key = screen.readInput() ?: continue

            action = when (key.kind) {
                Key.Kind.ArrowDown -> moveDownAction
                Key.Kind.ArrowUp -> moveUpAction
                Key.Kind.ArrowLeft -> moveLeftAction
                Key.Kind.ArrowRight -> moveRightAction
                Key.Kind.NormalKey -> when (key.character) {
                    'q' -> quitGameAction
                    's' -> saveGameAction
                    'l' -> loadGameAction
                    in '0'..'9' -> digitActionProvider?.actionFor(key.character.toInt() - '0'.toInt())
                    else -> null
                }
                else -> null
            }

            if (action != null) {
                break
            }
        }

        action!!.execute()
    }

    override fun draw(model: Model?) {
        currentModel = model ?: return
        screen.updateScreenSize()

        drawMap(currentModel!!)
        drawMobs(currentModel!!)
        drawPlayer(currentModel!!)
        drawInfoPanel(currentModel!!)

        screen.completeRefresh()
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
        val playerPosition = model.player.position
        screen.putString(
            MAP_POSITION_X + playerPosition.x,
            MAP_POSITION_Y + playerPosition.y,
            "p",
            Terminal.Color.GREEN,
            Terminal.Color.BLACK
        )
    }

    private fun drawMobs(model: Model) {
        for (mob in model.mobs) {
            val mobPosition = mob.position
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
        val player = model.player

        screen.putString(
            MAP_POSITION_X + map.getWidth() + INFO_PADDING_X,
            MAP_POSITION_Y + INFO_PADDING_Y,
            "level: " + player.level,
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

        val health = max(player.health, 0)

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
    }

    private fun drawControlInfo(x: Int, y: Int) {
        for ((i, message) in CONTROL_INFO.withIndex()) {
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

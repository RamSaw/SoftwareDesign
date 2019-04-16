package ru.hse.spb.model

import ru.hse.spb.controller.Controller.Companion.PlayerAction
import ru.hse.spb.controller.Controller.Companion.PlayerAction.*
import ru.hse.spb.model.Map.CellState.FREE
import ru.hse.spb.model.Map.CellState.OCCUPIED
import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.ConfusionPlayerDecorator
import ru.hse.spb.model.engine.GameCharacter
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player
import ru.hse.spb.model.engine.strategy.AggressiveStrategy
import ru.hse.spb.model.engine.strategy.FunkyStrategy
import ru.hse.spb.model.engine.strategy.PassiveStrategy
import ru.hse.spb.view.ConsoleView
import ru.hse.spb.view.View
import java.lang.Integer.max
import kotlin.random.Random


/**
 * This class implements core game mechanics and responses to user actions.
 */
class WorldModel(override val map: Map) : Model {
    override val player = ConfusionPlayerDecorator(Player(map.getStartCell()))
    override val mobs = mutableListOf<Mob>()
    override var currentRound = 0

    private val view: View = ConsoleView
    private val random = Random(0)
    private var currentCombatField: MapPosition? = null
    private val combatSystem = CombatSystem()

    init {
        decorateWithView { spawnMobs() }
    }

    private fun spawnMobs() {
        val pos = player.getCurrentPosition()
        val freeCells = map.getFreeCells()
        mobs.addAll(freeCells.shuffled(random).filter { !(it.x == pos.x && it.y == pos.y) }.take(
            random.nextInt(
                max(freeCells.size / MOBS_THRESHOLD, 1)
            )
        ).map {
            when (random.nextInt(5)) {
                1 -> Mob(currentRound, it, AggressiveStrategy(this))
                2 -> Mob(currentRound, it, PassiveStrategy())
                else -> Mob(currentRound, it, FunkyStrategy(this))
            }
        })
    }

    override fun move(action: PlayerAction) {
        if (player.getCurrentHealth() <= 0) {
            return
        }

        decorateWithView { movePlayer(action) }
        moveMobs()
        decorateWithView { combat() }
        decorateWithView { combatAftermath() }
        if (mobs.isEmpty()) {
            currentRound++
            player.levelUp()
            decorateWithView { spawnMobs() }
        }
    }

    private fun combat() {
        currentCombatField = combatSystem.combat(player, mobs)
    }

    private fun combatAftermath() {
        mobs.removeIf { it.getCurrentHealth() <= 0 }
    }

    private inline fun decorateWithView(move: () -> Unit) {
        move()
        view.draw(this)
    }

    private inline fun decorateWithPosChange(
        character: GameCharacter,
        move: (character: GameCharacter) -> Unit
    ) {
        map.changeCellState(character.getCurrentPosition(), FREE)
        move(character)
        map.changeCellState(character.getCurrentPosition(), OCCUPIED)
    }

    private fun movePlayer(action: PlayerAction) {
        var x = player.getCurrentPosition().x
        var y = player.getCurrentPosition().y

        when (action) {
            MOVE_UP -> y++
            MOVE_DOWN -> y--
            MOVE_LEFT -> x--
            MOVE_RIGHT -> x++
            TAKE_OFF_EQUIPMENT -> player.takeOffEquipment()
            TAKE_ON_EQUIPMENT -> player.takeOnEquipment()
            else -> {
            }
        }

        if (map.getCell(MapPosition(x, y)) == FREE)
            decorateWithPosChange(player) { player.changePosition(x, y) }
    }

    private fun moveMob(mob: Mob) {
        val nextPosition = mob.move(map)
        mob.changePosition(nextPosition.x, nextPosition.y)
    }

    private fun moveMobs() {
        for (mob in mobs) {
            decorateWithView { decorateWithPosChange(mob) { moveMob(mob) } }
        }
    }

    companion object {
        private const val MOBS_THRESHOLD = 5
    }
}
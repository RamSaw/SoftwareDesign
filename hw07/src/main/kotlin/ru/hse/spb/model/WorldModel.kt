package ru.hse.spb.model

import ru.hse.spb.controller.Controller.Companion.PlayerAction
import ru.hse.spb.controller.Controller.Companion.PlayerAction.*
import ru.hse.spb.model.Map.CellState.FREE
import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.DangerMob
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player
import ru.hse.spb.model.engine.SweetMob
import ru.hse.spb.view.ConsoleView
import ru.hse.spb.view.View
import kotlin.random.Random

class WorldModel(private val map: Map) : Model {
    private val view: View = ConsoleView
    private val random = Random(0)
    private val player = Player(map.getStartCell())
    private val mobs = mutableListOf<Mob>()
    private var currentRound = 0
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
                freeCells.size
            )
        ).map {
            if (random.nextInt(5) == 1) {
                DangerMob(currentRound, it)
            } else {
                SweetMob(currentRound, it)
            }
        })
    }

    override fun move(action: PlayerAction) {
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

    private fun movePlayer(action: PlayerAction) {
        val nextPosition = player.getCurrentPosition()
        when (action) {
            MOVE_UP -> nextPosition.y++
            MOVE_DOWN -> nextPosition.y--
            MOVE_LEFT -> nextPosition.x--
            MOVE_RIGHT -> nextPosition.x++
            TAKE_OFF_EQUIPMENT -> player.takeOffEquipment()
            TAKE_ON_EQUIPMENT -> player.takeOnEquipment()
        }

        if (map.getCell(nextPosition) == FREE)
            player.changePosition(nextPosition.x, nextPosition.y)
    }

    private fun moveMob(mob: Mob) {
        val nextPosition = mob.move(map)
        mob.changePosition(nextPosition.x, nextPosition.y)
    }

    private fun moveMobs() {
        for (mob in mobs) {
            decorateWithView { moveMob(mob) }
        }
    }
}
package ru.hse.spb.model

import ru.hse.spb.controller.Controller.Companion.PlayerAction
import ru.hse.spb.controller.Controller.Companion.PlayerAction.*
import ru.hse.spb.model.Map.CellState.FREE
import ru.hse.spb.model.Map.CellState.OCCUPIED
import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.*
import ru.hse.spb.view.View
import java.lang.Integer.max
import kotlin.random.Random


/**
 * This class implements core game mechanics and responses to user actions.
 */
class WorldModel(override val map: Map, override val view: View) : Model {
    companion object {
        private const val PERCENTAGE_OF_FIELD_SIZE_RANDOM_UNTIL = 5
        private const val DANGER_MOB_PROBABILITY_RANDOM_UNTIL = 5
    }

    override val player = Player(map.getStartCell())
    override val mobs = mutableListOf<Mob>()
    override var currentRound = 0

    private val random = Random(0)
    private val combatSystem = CombatSystem()

    init {
        decorateWithView { spawnMobs() }
    }

    private fun spawnMobs() {
        val pos = player.getCurrentPosition()
        val freeCells = map.getFreeCells()
        mobs.addAll(freeCells.shuffled(random).filter { it != pos }.take(
            random.nextInt(
                max(freeCells.size / PERCENTAGE_OF_FIELD_SIZE_RANDOM_UNTIL, 1)
            )
        ).map {
            if (random.nextInt(DANGER_MOB_PROBABILITY_RANDOM_UNTIL) == 1) {
                DangerMob(currentRound, it)
            } else {
                SweetMob(currentRound, it)
            }
        })
    }

    override fun move(action: PlayerAction) {
        if (player.getCurrentHealth() <= 0) {
            return
        }

        decorateWithView { movePlayer(action) }
        moveMobs()
        if (mobs.isEmpty()) {
            currentRound++
            player.levelUp()
            decorateWithView { spawnMobs() }
        }
    }

    private fun combatAftermath() {
        mobs.filter { it.getCurrentHealth() <= 0 }.forEach { map.changeCellState(it.getCurrentPosition(), FREE) }
        mobs.removeIf { it.getCurrentHealth() <= 0 }
    }

    private inline fun decorateWithPosChange(
        character: GameCharacter,
        move: (character: GameCharacter) -> Unit
    ) {
        map.changeCellState(character.getCurrentPosition(), FREE)
        move(character)
        map.changeCellState(character.getCurrentPosition(), OCCUPIED)
    }

    private inline fun decorateWithView(move: () -> Unit) {
        move()
        view.draw(this)
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
            else -> {}
        }

        if (map.getCell(MapPosition(x, y)) == FREE)
            decorateWithPosChange(player) { player.changePosition(x, y) }
        else if (map.getCell(MapPosition(x, y)) == OCCUPIED) {
            combatSystem.combat(player, mobs.first {
                it.getCurrentPosition().x == x
                        && it.getCurrentPosition().y == y
            })
            decorateWithView { combatAftermath() }
        }
    }

    private fun moveMob(mob: Mob) {
        val nextPosition = mob.move(map)
        if (map.getCell(nextPosition) == FREE) {
            mob.changePosition(nextPosition.x, nextPosition.y)
        } else {
            val fighter = mobs.firstOrNull {
                it.getCurrentPosition().x == nextPosition.x
                        && it.getCurrentPosition().y == nextPosition.y
            }
            if (fighter != null) {
                combatSystem.combat(fighter, mob)
            } else {
                combatSystem.combat(player, mob)
            }
            decorateWithView { combatAftermath() }
        }
    }

    private fun moveMobs() {
        for (mob in mobs) {
            decorateWithView { decorateWithPosChange(mob) { moveMob(mob) } }
        }
    }
}
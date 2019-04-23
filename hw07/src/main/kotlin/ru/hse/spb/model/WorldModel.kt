package ru.hse.spb.model

import ru.hse.spb.model.Map.CellState.FREE
import ru.hse.spb.model.Map.CellState.OCCUPIED
import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.ConfusionPlayerDecorator
import ru.hse.spb.model.engine.GameCharacter
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player
import ru.hse.spb.model.engine.strategy.AggressiveStrategy
import ru.hse.spb.model.engine.strategy.FunkyStrategy
import ru.hse.spb.model.engine.strategy.MobStrategy
import ru.hse.spb.model.engine.strategy.PassiveStrategy
import ru.hse.spb.view.ConsoleView
import ru.hse.spb.view.View
import java.io.*
import java.lang.Integer.max


/**
 * This class implements core game mechanics and responses to user actions.
 */
class WorldModel(override val map: Map) : Model, Serializable {
    override val player = ConfusionPlayerDecorator(Player(map.getStartCell()))
    override val mobs = mutableListOf<Mob>()
    override var currentRound = 0

    private val view: View = ConsoleView
    private val combatSystem = CombatSystem()

    private val strategies: Array<MobStrategy> =
        arrayOf(AggressiveStrategy(this), PassiveStrategy(), FunkyStrategy(this))

    private var gameFinished = false

    init {
        decorateWithView { spawnMobs() }
    }

    override fun isGameFinished(): Boolean {
        return gameFinished
    }

    override fun finishGame() {
        gameFinished = true
    }

    private fun spawnMobs() {
        val pos = player.getCurrentPosition()
        val freeCells = map.getFreeCells()
        mobs.addAll(freeCells.shuffled().filter { !(it.x == pos.x && it.y == pos.y) }.take(
            (0 until max(freeCells.size / MOBS_THRESHOLD, 1)).random()
        ).map {
            val strategy = strategies[(0 until strategies.size).random()]
            Mob(currentRound, it, strategy)
        })
    }

    private fun finishMove() {
        moveMobs()
        if (mobs.isEmpty()) {
            currentRound++
            player.levelUp()
            decorateWithView { spawnMobs() }
        }
        if (player.getCurrentHealth() <= 0) {
            gameFinished = true
        }
    }

    private fun combatAftermath() {
        mobs.filter { it.getCurrentHealth() <= 0 }.forEach { map.changeCellState(it.getCurrentPosition(), FREE) }
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

    override fun movePlayer(move: Model.PlayerMove) {
        var x = player.getCurrentPosition().x
        var y = player.getCurrentPosition().y

        when (move) {
            Model.PlayerMove.MOVE_UP -> y++
            Model.PlayerMove.MOVE_DOWN -> y--
            Model.PlayerMove.MOVE_LEFT -> x--
            Model.PlayerMove.MOVE_RIGHT -> x++
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

        decorateWithView { finishMove() }
    }

    override fun takeOnOffPlayerEquipment(equipmentId: Int) {
        player.takeOnOffEquipment(equipmentId)
        decorateWithView { finishMove() }
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

    companion object {
        private const val MOBS_THRESHOLD = 5
        private const val SAVED_GAME_FILENAME = "saves/savedGame"
        private const val FAILED_LOAD_MESSAGE = "Failed to load save file."

        fun save(game: WorldModel) {
            File(SAVED_GAME_FILENAME).parentFile.mkdirs()
            ObjectOutputStream(FileOutputStream(SAVED_GAME_FILENAME)).use {
                it.writeObject(game)
            }
        }

        fun load(): WorldModel {
            try {
                ObjectInputStream(FileInputStream(SAVED_GAME_FILENAME)).use {
                    return it.readObject() as WorldModel
                }
            } catch (e: Exception) {
                throw FailedLoadException(FAILED_LOAD_MESSAGE)
            }
        }
    }
}
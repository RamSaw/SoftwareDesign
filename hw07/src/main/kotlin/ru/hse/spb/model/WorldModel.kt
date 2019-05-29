package ru.hse.spb.model

import ru.hse.spb.model.Map.CellState.FREE
import ru.hse.spb.model.Map.CellState.OCCUPIED
import ru.hse.spb.model.engine.*
import ru.hse.spb.model.engine.strategy.AggressiveStrategy
import ru.hse.spb.model.engine.strategy.FunkyStrategy
import ru.hse.spb.model.engine.strategy.MobStrategy
import ru.hse.spb.model.engine.strategy.PassiveStrategy
import java.io.*
import java.lang.Integer.max


/**
 * This class implements core game mechanics and responses to user actions.
 */
class WorldModel(override var map: Map) : Model, Serializable {
    override fun getActivePlayer(): Int {
        return activePlayer
    }

    override fun addPlayer(): Int {
        val playerId = (players.keys.max() ?: -1) + 1
        players[playerId] = ConfusionPlayerDecorator(Player(map.getStartCell()))
        playerIds.add(playerId)
        if (activePlayer == -1) {
            activePlayer = 0
        }
        return playerId
    }

    override var players = mutableMapOf<Int, BasePlayer>()
    override var mobs = mutableListOf<Mob>()
    override var currentRound = 0

    private var combatSystem = CombatSystem()

    private var strategies: Array<MobStrategy> =
        arrayOf(AggressiveStrategy(this), PassiveStrategy(), FunkyStrategy(this))

    private var gameFinished = false
    private var gameStarted = false
    private var activePlayer = -1
    private var playerIds = mutableListOf<Int>()

    init {
        spawnMobs()
    }

    override fun isGameFinished(): Boolean {
        return gameFinished
    }

    override fun finishGame() {
        gameFinished = true
    }

    private fun spawnMobs() {
        val positions = players.map { it.value.getCurrentPosition() }
        val freeCells = map.getFreeCells()
        mobs.addAll(freeCells.shuffled().filter { it !in positions }.take(
            (0 until max(freeCells.size / MOBS_THRESHOLD, 1)).random()
        ).map {
            val strategy = strategies[(0 until strategies.size).random()]
            Mob(currentRound, it, strategy)
        })
    }

    private fun finishMove() {
        gameStarted = true
        moveMobs()
        activePlayer = (activePlayer + 1) % playerIds.size
        if (mobs.isEmpty()) {
            currentRound++
            players.values.forEach { it.levelUp() }
            spawnMobs()
        }
        if (players.values.none { it.getCurrentHealth() <= 0 }) {
            gameFinished = true
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

    override fun movePlayer(move: Model.PlayerMove) {
        val playerPosition = players[getActivePlayer()]!!.getCurrentPosition()

        when (move) {
            Model.PlayerMove.MOVE_UP -> playerPosition.y--
            Model.PlayerMove.MOVE_DOWN -> playerPosition.y++
            Model.PlayerMove.MOVE_LEFT -> playerPosition.x--
            Model.PlayerMove.MOVE_RIGHT -> playerPosition.x++
        }

        if (map.getCell(playerPosition) == FREE)
            players.values.forEach { player -> decorateWithPosChange(player) { player.changePosition(playerPosition.x, playerPosition.y) } }
        else if (map.getCell(playerPosition) == OCCUPIED) {
            players.values.forEach {player ->
                combatSystem.combat(player, mobs.first {
                    it.getCurrentPosition() == playerPosition
                })
                combatAftermath()
            }
        }

        finishMove()
    }

    override fun takeOnOffPlayerEquipment(equipmentId: Int) {
        players[getActivePlayer()]!!.takeOnOffEquipment(equipmentId)
        finishMove()
    }

    private fun moveMob(mob: Mob) {
        val nextPosition = mob.move(map)
        if (map.getCell(nextPosition) == FREE) {
            mob.changePosition(nextPosition.x, nextPosition.y)
        } else {
            val mobFighter = mobs.firstOrNull {
                it.getCurrentPosition() == nextPosition
            }
            if (mobFighter != null) {
                combatSystem.combat(mobFighter, mob)
            } else {
                val playerFigher = players.values.first {
                    it.getCurrentPosition() == nextPosition
                }
                combatSystem.combat(playerFigher, mob)
            }
            combatAftermath()
        }
    }

    private fun moveMobs() {
        for (mob in mobs) {
            decorateWithPosChange(mob) { moveMob(mob) }
        }
    }

    override fun save() {
        File(SAVED_GAME_FILENAME).parentFile.mkdirs()
        ObjectOutputStream(FileOutputStream(SAVED_GAME_FILENAME)).use {
            it.writeObject(this)
        }
    }

    override fun toByteArray(): ByteArray {
        val stream = ByteArrayOutputStream()
        ObjectOutputStream(stream).use {
            it.writeObject(this)
        }
        return stream.toByteArray()
    }

    override fun updateFromByteArray(byteArray: ByteArray) {
        try {
            ObjectInputStream(ByteArrayInputStream(byteArray)).use {
                val model = it.readObject() as WorldModel
                loadFromModel(model)
            }
        } catch (e: Exception) {
            throw FailedLoadException(FAILED_LOAD_MESSAGE)
        }
    }

    override fun load() {
        if (gameStarted) {
            return
        }

        try {
            ObjectInputStream(FileInputStream(SAVED_GAME_FILENAME)).use {
                val model = it.readObject() as WorldModel
                loadFromModel(model)
            }
        } catch (e: Exception) {
            throw FailedLoadException(FAILED_LOAD_MESSAGE)
        }
    }

    private fun loadFromModel(model: WorldModel) {
        this.map = model.map
        this.players = model.players
        this.mobs = model.mobs
        this.currentRound = model.currentRound
        this.combatSystem = model.combatSystem
        this.strategies = model.strategies
        this.gameFinished = model.gameFinished
        this.gameStarted = model.gameStarted
    }

    companion object {
        private const val MOBS_THRESHOLD = 5
        private const val SAVED_GAME_FILENAME = "saves/savedGame"
        private const val FAILED_LOAD_MESSAGE = "Failed to load save file."
    }
}
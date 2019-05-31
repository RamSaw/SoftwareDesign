package ru.hse.spb.model

import ru.hse.spb.model.engine.BasePlayer
import ru.hse.spb.model.engine.Mob

/**
 * This interface represents model of the game.
 */
interface Model {
    val players: kotlin.collections.Map<Int, BasePlayer>
    val map: Map
    val mobs: List<Mob>
    var currentRound: Int

    /**
     * Checks whether the game is finished or not.
     */
    fun isGameFinished() : Boolean

    /**
     * Changes model according to user action.
     */
    fun movePlayer(move: PlayerMove)

    /**
     * Takes on equipment if it is off and vice versa.
     */
    fun takeOnOffPlayerEquipment(equipmentId: Int)

    /**
     * Finishes game.
     */
    fun finishGame()

    /**
     * Saves state no the disk.
     */
    fun save()

    /**
     * Serializes byte array to disk.
     */
    fun toByteArray(): ByteArray

    /**
     * Creates model from byte array.
     */
    fun updateFromByteArray(byteArray: ByteArray)

    /**
     * Loads state from the disk.
     */
    fun load()

    fun getActivePlayer(): Int

    fun addPlayer(): Int

    fun removePlayer(playerId: Int)

    /**
     * Describes players move direction.
     */
    enum class PlayerMove {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
    }

    fun nextActivePlayer()
}

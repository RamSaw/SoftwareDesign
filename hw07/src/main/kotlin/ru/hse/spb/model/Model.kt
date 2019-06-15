package ru.hse.spb.model

import ru.hse.spb.model.engine.BasePlayer
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.view.View

/**
 * This interface represents model of the game.
 */
interface Model {
    val player: BasePlayer
    val map: Map
    val view: View
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
     * Loads state from the disk.
     */
    fun load()

    /**
     * Describes player move direction.
     */
    enum class PlayerMove {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
    }
}

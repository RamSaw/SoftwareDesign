package ru.hse.spb.model

import ru.hse.spb.model.engine.BasePlayer
import ru.hse.spb.model.engine.Mob

/**
 * This interface represents model of the game.
 */
interface Model {
    val player: BasePlayer
    val map: Map
    val mobs: List<Mob>
    var currentRound: Int

    fun isGameFinished() : Boolean

    /**
     * Changes model according to user action.
     */
    fun movePlayer(move: PlayerMove)

    fun takeOnOffPlayerEquipment(equipmentId: Int)

    fun finishGame()

    enum class PlayerMove {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
    }
}

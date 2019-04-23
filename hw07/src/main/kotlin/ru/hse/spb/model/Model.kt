package ru.hse.spb.model

import ru.hse.spb.controller.Controller.Companion.PlayerAction
import ru.hse.spb.model.engine.BasePlayer
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player

/**
 * This interface represents model of the game.
 */
interface Model {
    val player: BasePlayer
    val map: Map
    val mobs: List<Mob>
    var currentRound: Int

    /**
     * Changes model according to user action.
     */
    fun move(action: PlayerAction)
}

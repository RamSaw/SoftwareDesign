package ru.hse.spb.model

import ru.hse.spb.controller.Controller.Companion.PlayerAction
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player

interface Model {
    val map: Map
    val player: Player
    val mobs: List<Mob>
    var currentRound: Int

    fun move(action: PlayerAction)
}

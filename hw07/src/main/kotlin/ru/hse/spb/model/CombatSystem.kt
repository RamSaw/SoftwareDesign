package ru.hse.spb.model

import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player

class CombatSystem {
    fun combat(player: Player, mobs: List<Mob>): MapPosition? {
        mobs.filter { it.getCurrentPosition() == player.getCurrentPosition() }
            .forEach {
                it.takeDamage(player.inclineDamage())
                if (it.getCurrentHealth() > 0)
                    player.takeDamage(it.inclineDamage())
            }
        return if (mobs.any { it.getCurrentPosition() == player.getCurrentPosition() })
            player.getCurrentPosition()
        else
            null
    }
}
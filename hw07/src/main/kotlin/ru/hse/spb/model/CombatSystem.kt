package ru.hse.spb.model

import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player

/**
 * This class implements combat mechanics in the game.
 */
class CombatSystem {
    /**
     * Incline damage to player and mobs according to their positions.
     *
     * @return field where combat is conducted.
     */
    fun combat(player: Player, mobs: List<Mob>): MapPosition? {
        mobs.filter {
            it.getCurrentPosition().x == player.getCurrentPosition().x
                    && it.getCurrentPosition().y == player.getCurrentPosition().y
        }
            .forEach {
                it.takeDamage(player.inclineDamage())
                if (it.getCurrentHealth() > 0)
                    player.takeDamage(it.inclineDamage())
            }
        return if (mobs.any {
                it.getCurrentPosition().x == player.getCurrentPosition().x
                        && it.getCurrentPosition().y == player.getCurrentPosition().y
            })
            player.getCurrentPosition()
        else
            null
    }
}
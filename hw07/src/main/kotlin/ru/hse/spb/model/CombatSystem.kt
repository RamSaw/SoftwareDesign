package ru.hse.spb.model

import ru.hse.spb.model.engine.BasePlayer
import ru.hse.spb.model.engine.ConfusionPlayerDecorator
import ru.hse.spb.model.engine.Mob
import java.io.Serializable

/**
 * This class implements combat mechanics in the game.
 */
class CombatSystem : Serializable {

    /**
     * Incline damage to basic players and mob.
     */
    fun combat(player: BasePlayer, mob: Mob) {
        mob.takeDamage(player.inclineDamage())
        player.takeDamage(mob.inclineDamage())
    }

    /**
     * Incline damage to confusion players and mob.
     */
    fun combat(player: ConfusionPlayerDecorator, mob: Mob) {
        mob.takeDamage(player.inclineDamage())
        player.confuseAfterAttack(mob)
        player.takeDamage(mob.inclineDamage())
    }

    /**
     * Incline damage to mob and mob.
     */
    fun combat(bob: Mob, mob: Mob) {
        mob.takeDamage(bob.inclineDamage())
        bob.takeDamage(mob.inclineDamage())
    }
}
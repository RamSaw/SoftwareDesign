package ru.hse.spb.model

import ru.hse.spb.model.engine.GameCharacter
import ru.hse.spb.model.engine.ConfusionPlayerDecorator
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player
import java.io.Serializable

/**
 * This class implements combat mechanics in the game.
 */
class CombatSystem : Serializable {

    /**
     * Incline damage to basic player and mob.
     */
    fun combat(player: Player, mob: Mob) {
        mob.takeDamage(player.inclineDamage())
        player.takeDamage(mob.inclineDamage())
    }

    /**
     * Incline damage to confusion player and mob.
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
package ru.hse.spb.model

import ru.hse.spb.model.engine.GameCharacter
import ru.hse.spb.model.engine.ConfusionPlayerDecorator
import ru.hse.spb.model.engine.Mob
import java.io.Serializable

/**
 * This class implements combat mechanics in the game.
 */
class CombatSystem : Serializable {

    /**
     * Fight between game characters.
     */
    fun combat(aggressor: GameCharacter, victim: GameCharacter) {
        victim.takeDamage(aggressor.inclineDamage())
        aggressor.takeDamage(victim.inclineDamage())
    }

    /**
     * Incline damage to confusion player and mob.
     */
    fun combat(player: ConfusionPlayerDecorator, mob: Mob) {
        mob.takeDamage(player.inclineDamage())
        player.confuseAfterAttack(mob)
        player.takeDamage(mob.inclineDamage())
    }
}
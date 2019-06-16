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
        aggressor.attack(victim)
        victim.attack(aggressor)
    }
}
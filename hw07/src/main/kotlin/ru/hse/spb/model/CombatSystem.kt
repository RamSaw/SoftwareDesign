package ru.hse.spb.model

import ru.hse.spb.model.engine.GameCharacter

/**
 * This class implements combat mechanics in the game.
 */
class CombatSystem {

    /**
     * Fight between game characters.
     */
    fun combat(aggressor: GameCharacter, victim: GameCharacter) {
        victim.takeDamage(aggressor.inclineDamage())
        aggressor.takeDamage(victim.inclineDamage())
    }
}
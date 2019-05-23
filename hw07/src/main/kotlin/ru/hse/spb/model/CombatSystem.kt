package ru.hse.spb.model

import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.GameCharacter

/**
 * This class implements combat mechanics in the game.
 */
class CombatSystem {
    /**
     * Incline damage to attackingCharacter and attackedCharacters according to their positions.
     *
     * @return field where combat is conducted.
     */
    fun combat(attackingCharacter: GameCharacter, attackedCharacters: List<GameCharacter>): MapPosition? {
        attackedCharacters.filter {
            it.getCurrentPosition() == attackingCharacter.getCurrentPosition()
        }.forEach {
            it.takeDamage(attackingCharacter.inclineDamage())
        }
        return if (attackedCharacters.any {
                it.getCurrentPosition() == attackingCharacter.getCurrentPosition()
            })
            attackingCharacter.getCurrentPosition()
        else
            null
    }
}
package ru.hse.spb.model.engine.strategy

import ru.hse.spb.model.Map
import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.Mob

/**
 * This interface represents mob strategy.
 */
interface MobStrategy {
    /**
     * Decides where to move mob.
     */
    fun makeTurn(mob: Mob, map: Map): MapPosition

    /**
     * Checks whether this strategy is still actual or not.
     */
    fun isExpired(): Boolean
}

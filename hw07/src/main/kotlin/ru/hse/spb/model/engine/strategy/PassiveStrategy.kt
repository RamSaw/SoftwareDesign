package ru.hse.spb.model.engine.strategy

import ru.hse.spb.model.Map
import ru.hse.spb.model.engine.Mob

/**
 * Passive strategy - mob stands still.
 */
class PassiveStrategy : MobStrategy {
    override fun makeTurn(mob: Mob, map: Map): Map.MapPosition {
        return mob.position
    }

    override fun isExpired(): Boolean {
        return false
    }
}

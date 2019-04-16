package ru.hse.spb.model.engine.strategy

import ru.hse.spb.model.Map
import ru.hse.spb.model.engine.Mob

class PassiveStrategy : MobStrategy {
    override fun makeTurn(mob: Mob, map: Map): Map.MapPosition {
        return mob.getCurrentPosition()
    }

    override fun isExpired(): Boolean {
        return false
    }
}

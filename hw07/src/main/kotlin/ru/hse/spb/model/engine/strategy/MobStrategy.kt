package ru.hse.spb.model.engine.strategy

import ru.hse.spb.model.Map
import ru.hse.spb.model.Map.MapPosition
import ru.hse.spb.model.engine.Mob

interface MobStrategy {
    fun makeTurn(mob: Mob, map: Map): MapPosition
    fun isExpired(): Boolean
}

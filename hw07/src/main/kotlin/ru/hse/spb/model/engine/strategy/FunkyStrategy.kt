package ru.hse.spb.model.engine.strategy

import ru.hse.spb.model.Map
import ru.hse.spb.model.Model
import ru.hse.spb.model.engine.Mob

class FunkyStrategy(private val model: Model) : MobStrategy {
    override fun makeTurn(mob: Mob, map: Map): Map.MapPosition {

    }

    override fun isExpired(): Boolean {
        return false
    }
}

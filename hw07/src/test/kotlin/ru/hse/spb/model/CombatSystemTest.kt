package ru.hse.spb.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import ru.hse.spb.model.engine.ConfusionPlayerDecorator
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player
import ru.hse.spb.model.engine.strategy.ConfusedStrategy

class CombatSystemTest {

    @Test
    fun testBasicPlayer() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.health
        val mob = Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100))
        val combatSystem = CombatSystem()
        combatSystem.combat(player, mob)
        assertEquals(oldHealth - mob.inclineDamage(), player.health)
    }

    @Test
    fun testConfusionPlayer() {
        val player = ConfusionPlayerDecorator(Player(Map.MapPosition(0, 0)))
        val oldHealth = player.health
        val mob = Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100))
        val combatSystem = CombatSystem()
        combatSystem.combat(player, mob)
        assertEquals(oldHealth - mob.inclineDamage(), player.health)
    }

    @Test
    fun testFightMobs() {
        val bob = Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100))
        val oldHealthBob = bob.health
        val mob = Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100))
        val oldHealthMob = mob.health
        val combatSystem = CombatSystem()
        combatSystem.combat(bob, mob)
        assertEquals(oldHealthBob - mob.inclineDamage(), bob.health)
        assertEquals(oldHealthMob - bob.inclineDamage(), mob.health)
    }
}
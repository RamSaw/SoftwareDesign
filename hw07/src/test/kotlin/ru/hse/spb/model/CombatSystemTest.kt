package ru.hse.spb.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import ru.hse.spb.model.engine.ConfusionPlayerDecorator
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player
import ru.hse.spb.model.engine.strategy.ConfusedStrategy

class CombatSystemTest {

    @Test
    fun testBasicPlayer() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.getCurrentHealth()
        val mob = Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100))
        val combatSystem = CombatSystem()
        combatSystem.combat(player, mob)
        assertEquals(oldHealth - mob.inclineDamage(), player.getCurrentHealth())
        assertNull(mob.getStrategy())
    }

    @Test
    fun testConfusionPlayer() {
        val player = ConfusionPlayerDecorator(Player(Map.MapPosition(0, 0)))
        val oldHealth = player.getCurrentHealth()
        val mob = Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100))
        val combatSystem = CombatSystem()
        combatSystem.combat(player, mob)
        assertEquals(oldHealth - mob.inclineDamage(), player.getCurrentHealth())
        assertEquals(ConfusedStrategy(0).javaClass, mob.getStrategy()?.javaClass)
    }

    @Test
    fun testFightMobs() {
        val bob = Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100))
        val oldHealthBob = bob.getCurrentHealth()
        val mob = Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100))
        val oldHealthMob = mob.getCurrentHealth()
        val combatSystem = CombatSystem()
        combatSystem.combat(bob, mob)
        assertEquals(oldHealthBob - mob.inclineDamage(), bob.getCurrentHealth())
        assertEquals(oldHealthMob - bob.inclineDamage(), mob.getCurrentHealth())
    }
}
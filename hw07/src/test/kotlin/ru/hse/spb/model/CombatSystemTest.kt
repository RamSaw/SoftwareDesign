package ru.hse.spb.model

import org.junit.Assert.*
import org.junit.Test
import ru.hse.spb.model.engine.Mob
import ru.hse.spb.model.engine.Player
import ru.hse.spb.model.engine.strategy.ConfusedStrategy

class CombatSystemTest {

    @Test
    fun testNoCombat() {
        val player = Player(Map.MapPosition(0, 0))
        val mobs = listOf(Mob(1, Map.MapPosition(1, 1), ConfusedStrategy(100)))
        val combatSystem = CombatSystem()
        val field = combatSystem.combat(player, mobs)
        assertNull(field)
    }

    @Test
    fun testOneMob() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.getCurrentHealth()
        val mobs = listOf(Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100)))
        val combatSystem = CombatSystem()
        val field = combatSystem.combat(player, mobs)
        assertNotNull(field)
        assertEquals(oldHealth - mobs[0].inclineDamage(), player.getCurrentHealth())
    }

    @Test
    fun testTenMobs() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.getCurrentHealth()
        val mobs = Array(10) { Mob(1, Map.MapPosition(0, 0), ConfusedStrategy(100)) }.toList()
        val combatSystem = CombatSystem()
        val field = combatSystem.combat(player, mobs)
        assertNotNull(field)
        assertEquals(oldHealth - mobs[0].inclineDamage() * 10, player.getCurrentHealth())
    }
}
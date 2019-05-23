package ru.hse.spb.model

import org.junit.Assert.*
import org.junit.Test
import ru.hse.spb.model.engine.DangerMob
import ru.hse.spb.model.engine.Player

class CombatSystemTest {

    @Test
    fun testNoCombat() {
        val player = Player(Map.MapPosition(0, 0))
        val mobs = listOf(DangerMob(1, Map.MapPosition(1, 1)))
        val combatSystem = CombatSystem()
        val field = combatSystem.combat(player, mobs)
        assertNull(field)
    }

    @Test
    fun testOneMob() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.getCurrentHealth()
        val mobs = listOf(DangerMob(1, Map.MapPosition(0, 0)))
        val combatSystem = CombatSystem()
        val field1 = combatSystem.combat(player, mobs)
        assertNotNull(field1)
        val field2 = combatSystem.combat(mobs[0], listOf(player))
        assertEquals(field1, field2)
        assertEquals(oldHealth - mobs[0].inclineDamage(), player.getCurrentHealth())
    }

    @Test
    fun testTenMobs() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.getCurrentHealth()
        val mobs = Array(10) { DangerMob(1, Map.MapPosition(0, 0)) }.toList()
        val combatSystem = CombatSystem()
        val field1 = combatSystem.combat(player, mobs)
        assertNotNull(field1)
        for (mob in mobs) {
            val field2 = combatSystem.combat(mob, listOf(player))
            assertEquals(field1, field2)
        }
        assertEquals(oldHealth - mobs[0].inclineDamage() * 10, player.getCurrentHealth())
    }
}
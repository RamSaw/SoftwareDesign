package ru.hse.spb.model

import org.junit.Assert.assertEquals
import org.junit.Test
import ru.hse.spb.model.engine.DangerMob
import ru.hse.spb.model.engine.Player

class CombatSystemTest {

    @Test
    fun testBasicPlayer() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.getCurrentHealth()
        val mob = DangerMob(1, Map.MapPosition(0, 0))
        val combatSystem = CombatSystem()
        combatSystem.combat(player, mob)
        assertEquals(oldHealth - mob.inclineDamage(), player.getCurrentHealth())
    }

    @Test
    fun testFightMobs() {
        val bob = DangerMob(1, Map.MapPosition(0, 0))
        val oldHealthBob = bob.getCurrentHealth()
        val mob = DangerMob(1, Map.MapPosition(0, 0))
        val oldHealthMob = mob.getCurrentHealth()
        val combatSystem = CombatSystem()
        combatSystem.combat(bob, mob)
        assertEquals(oldHealthBob - mob.inclineDamage(), bob.getCurrentHealth())
        assertEquals(oldHealthMob - bob.inclineDamage(), mob.getCurrentHealth())
    }
}
package ru.hse.spb.model.engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.hse.spb.model.Map

class PlayerTest {

    @Test
    fun testTakeOffTakeOnEquipment() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.health
        val oldStrength = player.inclineDamage()
        player.takeOnOffEquipment(0)
        player.takeOnOffEquipment(0)
        assertEquals(oldHealth, player.health)
        assertEquals(oldStrength, player.inclineDamage())
    }

    @Test
    fun levelUp() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.health
        val oldStrength = player.inclineDamage()
        player.levelUp()
        assertTrue(oldStrength < player.inclineDamage())
        assertTrue(oldHealth < player.health)
        assertTrue(player.level == 2)
    }
}
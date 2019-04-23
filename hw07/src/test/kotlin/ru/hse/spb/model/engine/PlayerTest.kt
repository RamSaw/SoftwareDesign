package ru.hse.spb.model.engine

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import ru.hse.spb.model.Map

class PlayerTest {

    @Test
    fun testTakeOffEquipmentTwoTimes() {
        val player = Player(Map.MapPosition(0, 0))
        player.takeOffEquipment()
        val oldHealth = player.getCurrentHealth()
        val oldStrength = player.inclineDamage()
        player.takeOffEquipment()
        assertEquals(oldHealth, player.getCurrentHealth())
        assertEquals(oldStrength, player.inclineDamage())
    }

    @Test
    fun testTakeOnEquipmentTwoTimes() {
        val player = Player(Map.MapPosition(0, 0))
        player.takeOnEquipment()
        val oldHealth = player.getCurrentHealth()
        val oldStrength = player.inclineDamage()
        player.takeOnEquipment()
        assertEquals(oldHealth, player.getCurrentHealth())
        assertEquals(oldStrength, player.inclineDamage())
    }

    @Test
    fun testTakeOffTakeOnEquipment() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.getCurrentHealth()
        val oldStrength = player.inclineDamage()
        player.takeOffEquipment()
        player.takeOnEquipment()
        assertEquals(oldHealth, player.getCurrentHealth())
        assertEquals(oldStrength, player.inclineDamage())
    }

    @Test
    fun levelUp() {
        val player = Player(Map.MapPosition(0, 0))
        val oldHealth = player.getCurrentHealth()
        val oldStrength = player.inclineDamage()
        player.levelUp()
        assertTrue(oldStrength < player.inclineDamage())
        assertTrue(oldHealth < player.getCurrentHealth())
        assertTrue(player.getCurrentLevel() == 2)
    }
}
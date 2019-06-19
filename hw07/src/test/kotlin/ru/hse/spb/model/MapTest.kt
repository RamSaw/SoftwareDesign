package ru.hse.spb.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

class MapTest {

    @Test
    fun testGenerate() {
        val map = Map.generate()
        val freeCells = map.getFreeCells()

        for (cellPosition in freeCells) {
            assertNotEquals(0, cellPosition.x)
            assertNotEquals(map.field.size - 1, cellPosition.x)
            assertNotEquals(0, cellPosition.y)
            assertNotEquals(map.getWidth() - 1, cellPosition.y)
        }
    }

    @Test
    fun testLoad() {
        val map = Map.load(javaClass.getResource("/maps/map_test.txt").path)
        val expectedCells = listOf(Map.MapPosition(1, 1),
                                   Map.MapPosition(2, 1),
                                   Map.MapPosition(2, 2))
        val actualCells = map.getFreeCells()

        assertEquals(expectedCells.size, actualCells.size)
        for (i in expectedCells.indices) {
            assertEquals(expectedCells[i].x, actualCells[i].x)
            assertEquals(expectedCells[i].y, actualCells[i].y)
        }
    }

    @Test
    fun testGetWidth() {
        val map = Map.load(javaClass.getResource("/maps/map_test.txt").path)
        val expected = 4

        assertEquals(expected, map.getWidth())
    }

    @Test
    fun testGetStartCell() {
        val map = Map.load(javaClass.getResource("/maps/map_test.txt").path)
        val startCell = map.getStartCell()

        assertEquals(Map.CellState.FREE, map.getCell(startCell))
    }
}

package ru.hse.spb.model

import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File

class WorldModelTest {
    private val filename = "saves/savedGame"

    @Test
    fun testSaveNoSaveFile() {
        File(filename).deleteRecursively()
        assertThrows<FailedLoadException> { WorldModel.load() }
    }

    @Test
    fun testBadSaveFile() {
        File(filename).parentFile.mkdirs()
        File(filename).writeText("KEK")
        assertThrows<FailedLoadException> { WorldModel.load() }

    }

    @Test
    fun testSave() {
        val model = WorldModel(Map.generate())
        WorldModel.save(model)
        val savedModel = WorldModel.load()
        assertArrayEquals(savedModel.map.field, model.map.field)
    }

    @Test
    fun testTwoSaves() {
        val model = WorldModel(Map.generate())
        WorldModel.save(model)
        val savedModel = WorldModel.load()
        WorldModel.save(model)
        val sameModel = WorldModel.load()
        assertArrayEquals(savedModel.map.field, sameModel.map.field)
    }
}
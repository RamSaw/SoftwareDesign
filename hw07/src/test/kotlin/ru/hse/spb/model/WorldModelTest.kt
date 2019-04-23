package ru.hse.spb.model

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class WorldModelTest {
    private val filename = "saves/savedGame"

    @BeforeEach
    @AfterEach
    fun deleteSave() {
        while (!File(filename).deleteRecursively()) {
        }
    }

    @Test
    fun testSaveNoSaveFile() {
        assertFalse(Files.exists(Paths.get(filename)))
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
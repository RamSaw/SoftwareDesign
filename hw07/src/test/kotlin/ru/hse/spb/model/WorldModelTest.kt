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
        val model = WorldModel(Map.generate())
        assertFalse(Files.exists(Paths.get(filename)))
        assertThrows<FailedLoadException> { model.load() }
    }

    @Test
    fun testBadSaveFile() {
        val model = WorldModel(Map.generate())
        File(filename).parentFile.mkdirs()
        File(filename).writeText("KEK")
        assertThrows<FailedLoadException> { model.load() }
    }

    @Test
    fun testSave() {
        val model = WorldModel(Map.generate())
        model.save()
        val newModel = WorldModel(Map.generate())
        newModel.load()
        assertArrayEquals(newModel.map.field, model.map.field)
    }

    @Test
    fun testTwoSaves() {
        val model = WorldModel(Map.generate())
        model.save()
        val newModel = WorldModel(Map.generate())
        newModel.load()

        model.save()
        val sameModel = WorldModel(Map.generate())
        sameModel.load()
        assertArrayEquals(newModel.map.field, sameModel.map.field)
    }
}
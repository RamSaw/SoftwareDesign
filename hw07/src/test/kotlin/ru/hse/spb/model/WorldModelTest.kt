package ru.hse.spb.model

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import ru.hse.spb.view.View
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

class WorldModelTest {
    private val filename = "saves/savedGame"
    private var view: View = mock(View::class.java)

    @BeforeEach
    @AfterEach
    fun deleteSave() {
        while (!File(filename).deleteRecursively()) {
        }
    }

    @Test
    fun testSaveNoSaveFile() {
        val model = WorldModel(Map.generate(), view)
        assertFalse(Files.exists(Paths.get(filename)))
        assertThrows<FailedLoadException> { model.load() }
    }

    @Test
    fun testBadSaveFile() {
        val model = WorldModel(Map.generate(), view)
        File(filename).parentFile.mkdirs()
        File(filename).writeText("KEK")
        assertThrows<FailedLoadException> { model.load() }
    }

    @Test
    fun testSave() {
        val model = WorldModel(Map.generate(), view)
        model.save()
        val newModel = WorldModel(Map.generate(), view)
        newModel.load()
        assertArrayEquals(newModel.map.field, model.map.field)
    }

    @Test
    fun testTwoSaves() {
        val model = WorldModel(Map.generate(), view)
        model.save()
        val newModel = WorldModel(Map.generate(), view)
        newModel.load()

        model.save()
        val sameModel = WorldModel(Map.generate(), view)
        sameModel.load()
        assertArrayEquals(newModel.map.field, sameModel.map.field)
    }
}
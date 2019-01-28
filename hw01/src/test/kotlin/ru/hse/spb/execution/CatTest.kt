package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.nio.file.Paths

class CatTest {
    @Test
    fun catNoArgument() {
        val expected = ""
        assertEquals(expected, Cat(listOf(), null).execute())
    }

    @Test
    fun catOneArgument() {
        val testFilePath = Paths.get(CatTest::class.java.getResource("/ru/hse/spb/testFile1").path)
        val expected = "contentOfTestFile1"
        assertEquals(expected, Cat(listOf(testFilePath), null).execute())
    }

    @Test
    fun catTwoArguments() {
        val testFile1Path = Paths.get(CatTest::class.java.getResource("/ru/hse/spb/testFile1").path)
        val testFile2Path = Paths.get(CatTest::class.java.getResource("/ru/hse/spb/testFile2").path)
        val expected = "contentOfTestFile1" + System.lineSeparator() + "content Of" + System.lineSeparator() + "Test File 2"
        assertEquals(expected, Cat(listOf(testFile1Path, testFile2Path), null).execute())
    }
}
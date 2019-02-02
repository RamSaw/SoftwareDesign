package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.getResourceFilePath

class CatTest {
    @Test
    fun catNoArgument() {
        val expected = ""
        assertEquals(expected, Cat(listOf(), null).execute())
    }

    @Test
    fun catOneArgument() {
        val testFilePath = getResourceFilePath("testFile1")
        val expected = "contentOfTestFile1"
        assertEquals(expected, Cat(listOf(testFilePath), null).execute())
    }

    @Test
    fun catTwoArguments() {
        val testFile1Path = getResourceFilePath("testFile1")
        val testFile2Path = getResourceFilePath("testFile2")
        val expected = "contentOfTestFile1\ncontent Of\nTest File 2"
        assertEquals(expected, Cat(listOf(testFile1Path, testFile2Path), null).execute())
    }
}
package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.getResourceFilePath

class WcTest {
    @Test
    fun wcNoArgument() {
        val expected = "0 0 0"
        assertEquals(expected, Wc(listOf(), null).execute())
    }

    @Test
    fun wcOneArgument() {
        val testFilePath = getResourceFilePath("testFile1")
        val expected = "0 1 18 testFile1"
        assertEquals(expected, Wc(listOf(testFilePath), null).execute())
    }

    @Test
    fun wcTwoArguments() {
        val testFile1Path = getResourceFilePath("testFile1")
        val testFile2Path = getResourceFilePath("testFile2")
        val expected = "0 1 18 testFile1\n1 5 22 testFile2\n1 6 40 total"
        assertEquals(expected, Wc(listOf(testFile1Path, testFile2Path), null).execute())
    }

    @Test
    fun wcPipeline() {
        val testFile1Path = getResourceFilePath("testFile1")
        val expected = "0 1 18"
        assertEquals(expected, Wc(listOf(), Cat(listOf(testFile1Path), null)).execute())
    }
}
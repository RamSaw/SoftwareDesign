package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.getResourceFilePath

class WcTest {
    @Test
    fun wcNoArgument() {
        val expected = "0 0 0\n"
        assertEquals(expected, Wc(listOf(), null).execute())
    }

    @Test
    fun wcOneArgument() {
        val testFilePath = getResourceFilePath("testFile1")
        val expected = "0 1 18 testFile1\n"
        assertEquals(expected, Wc(listOf(testFilePath), null).execute())
    }

    @Test
    fun wcTwoArguments() {
        val testFile1Path = getResourceFilePath("testFile1")
        val testFile2Path = getResourceFilePath("testFile2")
        val testFile2BytesNumber = 21 + System.lineSeparator().length
        val bothFilesBytesNumber = 18 + testFile2BytesNumber
        val expected = "0 1 18 testFile1\n1 5 $testFile2BytesNumber testFile2\n1 6 $bothFilesBytesNumber total\n"
        assertEquals(expected, Wc(listOf(testFile1Path, testFile2Path), null).execute())
    }

    @Test
    fun wcPipeline() {
        val testFile1Path = getResourceFilePath("testFile1")
        val expected = "0 1 18\n"
        assertEquals(expected, Wc(listOf(), Cat(listOf(testFile1Path), null)).execute())
    }
}
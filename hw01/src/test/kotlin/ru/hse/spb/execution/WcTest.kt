package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.nio.file.Paths

class WcTest {
    @Test
    fun wcNoArgument() {
        val expected = "0 0 0"
        assertEquals(expected, Wc(listOf(), null).execute())
    }

    @Test
    fun wcOneArgument() {
        val testFilePath = Paths.get(CatTest::class.java.getResource("/ru/hse/spb/testFile1").path)
        val expected = "0 1 18 testFile1"
        assertEquals(expected, Wc(listOf(testFilePath), null).execute())
    }

    @Test
    fun wcTwoArguments() {
        val testFile1Path = Paths.get(CatTest::class.java.getResource("/ru/hse/spb/testFile1").path)
        val testFile2Path = Paths.get(CatTest::class.java.getResource("/ru/hse/spb/testFile2").path)
        val expected = "0 1 18 testFile1" + System.lineSeparator() +
                "1 5 22 testFile2" + System.lineSeparator() + "1 6 40 total"
        assertEquals(expected, Wc(listOf(testFile1Path, testFile2Path), null).execute())
    }

    @Test
    fun wcPipeline() {
        val testFile1Path = Paths.get(CatTest::class.java.getResource("/ru/hse/spb/testFile1").path)
        val expected = "0 1 18"
        assertEquals(expected, Wc(listOf(), Cat(listOf(testFile1Path), null)).execute())
    }
}
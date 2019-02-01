package ru.hse.spb

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.execution.CatTest
import java.nio.file.Paths

/**
 * Test examples from presentation
 */
class CliApplicationTest {
    @Test
    fun process() {
        val exampleTxt =
            Paths.get(CatTest::class.java.getResource("/ru/hse/spb/example.txt").path).toAbsolutePath()
        assertEquals("Hello, world!" + System.lineSeparator(), CliApplication.process("echo \"Hello, world!\""))
        assertEquals("", CliApplication.process("FILE=$exampleTxt"))
        assertEquals("Some example text" + System.lineSeparator(), CliApplication.process("cat \$FILE"))
        assertEquals("1 3 18", CliApplication.process("cat $exampleTxt | wc"))
        assertEquals("1 3 18 example.txt", CliApplication.process("wc $exampleTxt"))
        assertEquals("1 1 4", CliApplication.process("echo 123 | wc"))
        assertEquals("", CliApplication.process("x=exit"))
        // assertEquals("", CliApplication.process("\$x")) exits test
    }

    @Test
    fun testDoubleQuotesInCat() {
        val exampleTxt =
            Paths.get(CatTest::class.java.getResource("/ru/hse/spb/example.txt").path).toAbsolutePath()
        val exampleTxtWithDoubleQuotes =
            "${Paths.get(exampleTxt.parent.toAbsolutePath().toString(), "\"exam\"ple.txt")}"
        assertEquals(
            "Some example text" + System.lineSeparator(),
            CliApplication.process("cat $exampleTxtWithDoubleQuotes")
        )
    }

    @Test
    fun pipelineWithoutSpaces() {
        assertEquals("1 2 10", CliApplication.process("echo \"some text\"|wc"))
    }
}
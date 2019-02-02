package ru.hse.spb

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.exceptions.ExternalCommandException
import ru.hse.spb.exceptions.IncorrectQuotingException
import ru.hse.spb.exceptions.UnknownCommandException
import ru.hse.spb.execution.CatTest
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

fun getResourceFilePath(filename: String): Path {
    throw Exception(File.separator + Paths.get("ru", "hse", "spb", filename).toString())
    Paths.get(
        CatTest::class.java.getResource(
            File.separator + Paths.get("ru", "hse", "spb", filename).toString()
        ).path
    )
}

/**
 * Test examples from presentation
 */
class CliApplicationTest {
    @Test
    fun process() {
        val exampleTxt = getResourceFilePath("example.txt").toAbsolutePath()
        assertEquals(
            "Hello, world!" + "\n",
            CliApplication.process("echo \"Hello, world!\"")
        )
        assertEquals("", CliApplication.process("FILE=$exampleTxt"))
        assertEquals("Some example text" + "\n", CliApplication.process("cat \$FILE"))
        assertEquals("1 3 18", CliApplication.process("cat $exampleTxt | wc"))
        assertEquals("1 3 18 example.txt", CliApplication.process("wc $exampleTxt"))
        assertEquals("1 1 4", CliApplication.process("echo 123 | wc"))
        assertEquals("", CliApplication.process("x=exit"))
        // assertEquals("", CliApplication.process("\$x")) exits test
    }

    @Test
    fun testDoubleQuotesInCat() {
        val exampleTxt =
            getResourceFilePath("example.txt").toAbsolutePath()
        val exampleTxtWithDoubleQuotes =
            "${Paths.get(exampleTxt.parent.toAbsolutePath().toString(), "\"exam\"ple.txt")}"
        assertEquals(
            "Some example text\n", CliApplication.process("cat $exampleTxtWithDoubleQuotes")
        )
    }

    @Test
    fun pipelineWithoutSpaces() {
        assertEquals("1 2 10", CliApplication.process("echo \"some text\"|wc"))
    }

    @Test(expected = IncorrectQuotingException::class)
    fun quotingMustBeClosed() {
        CliApplication.process("echo \"text")
    }

    @Test
    fun correctInterpolationWithTwoVariables() {
        assertEquals("", CliApplication.process("a=ec"))
        assertEquals("", CliApplication.process("b=ho"))
        assertEquals("text\n", CliApplication.process("\$a\$b text"))
    }

    @Test(expected = UnknownCommandException::class)
    fun incorrectPipelineThrowsException() {
        CliApplication.process("echo text | ")
    }

    @Test
    fun singleQuotesInDoubleQuotes() {
        assertEquals("", CliApplication.process("t=text"))
        assertEquals("'text'\n", CliApplication.process("echo \"'\$t'\""))
    }

    @Test
    fun pipeInSingeQuotes() {
        assertEquals("wesd|\n", CliApplication.process("echo \"wesd|\""))
    }

    @Test(expected = ExternalCommandException::class)
    fun doubleQuotesInAssignment() {
        CliApplication.process("\"\"=text")
    }

    @Test
    fun pipelineAndInterpolation() {
        assertEquals("", CliApplication.process("a=ec"))
        assertEquals("", CliApplication.process("b=ho"))
        assertEquals("", CliApplication.process("c=te"))
        assertEquals("", CliApplication.process("d=xt"))
        assertEquals("", CliApplication.process("f=cat"))
        assertEquals("text\n", CliApplication.process("\$a\$b \$c\$d|\$f"))
    }
}
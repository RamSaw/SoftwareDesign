package ru.hse.spb.parser

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.environment.GlobalEnvironment
import ru.hse.spb.exceptions.UnknownCommandException
import ru.hse.spb.exceptions.WrongCommandArgumentsException
import ru.hse.spb.execution.*
import java.nio.file.Paths

class ExecutionParserTest {
    private fun <T : Executable> testParsing(tokens: List<String>, expected: Executable) {
        @Suppress("UNCHECKED_CAST")
        assertEquals(expected, ExecutionParser.parse(tokens) as T)
    }

    @Test
    fun echoString() {
        testParsing<Echo>(listOf("echo", "Hello, World!"), Echo(listOf("Hello, World!"), null))
    }

    @Test
    fun assignment() {
        testParsing<Assignment>(listOf("=", "FILE", "example.txt"),
            Assignment("FILE", "example.txt", true, null)
        )
    }

    @Test
    fun cat() {
        testParsing<Cat>(listOf("cat", "example.txt"), Cat(listOf(Paths.get("example.txt")), null))
    }

    @Test
    fun catPipeWc() {
        testParsing<Wc>(listOf("cat", "example.txt", "|", "wc"),
            Wc(listOf(), Cat(listOf(Paths.get("example.txt")), null)))
    }

    @Test
    fun echoPipeWc() {
        testParsing<Wc>(listOf("echo", "123", "|", "wc"), Wc(listOf(), Echo(listOf("123"), null)))
    }

    @Test
    fun assignmentExit() {
        testParsing<Assignment>(listOf("=", "x", "exit"),
            Assignment("x", "exit", true, null)
        )
    }

    @Test
    fun exitSubstitution() {
        GlobalEnvironment.setVariable("x", "exit")
        testParsing<Exit>(listOf("exit"), Exit(true, null))
    }

    @Test(expected = WrongCommandArgumentsException::class)
    fun assignmentThrowsWrongCommandArgumentsException() {
        ExecutionParser.parse(listOf("=", "1", "2", "3"))
    }

    @Test(expected = UnknownCommandException::class)
    fun emptyCommandThrowsException() {
        ExecutionParser.parse(listOf("|"))
    }
}
package ru.hse.spb.lexer

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.environment.GlobalEnvironment

class InterpolationLexerTest {
    @Test
    fun echoString() {
        val result = InterpolationLexer.tokenize("echo \"Hello, World!\"")
        val expectedResult = listOf("echo", "Hello, World!")
        assertEquals(expectedResult, result)
    }

    @Test
    fun assignment() {
        val result = InterpolationLexer.tokenize("FILE=example.txt")
        val expectedResult = listOf("=", "FILE", "example.txt")
        assertEquals(expectedResult, result)
    }

    @Test
    fun cat() {
        GlobalEnvironment.setVariable("FILE", "example.txt")
        val result = InterpolationLexer.tokenize("cat \$FILE")
        val expectedResult = listOf("cat", "example.txt")
        assertEquals(expectedResult, result)
    }

    @Test
    fun catPipeWc() {
        val result = InterpolationLexer.tokenize("cat example.txt | wc")
        val expectedResult = listOf("cat", "example.txt", "|", "wc")
        assertEquals(expectedResult, result)
    }

    @Test
    fun echoPipeWc() {
        val result = InterpolationLexer.tokenize("echo 123 | wc")
        val expectedResult = listOf("echo", "123", "|", "wc")
        assertEquals(expectedResult, result)
    }

    @Test
    fun assignmentExit() {
        val result = InterpolationLexer.tokenize("x=exit")
        val expectedResult = listOf("=", "x", "exit")
        assertEquals(expectedResult, result)
    }

    @Test
    fun exitSubstitution() {
        GlobalEnvironment.setVariable("x", "exit")
        val result = InterpolationLexer.tokenize("\$x")
        val expectedResult = listOf("exit")
        assertEquals(expectedResult, result)
    }
}
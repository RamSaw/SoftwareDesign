package ru.hse.spb.lexer

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.environment.GlobalEnvironment
import ru.hse.spb.exceptions.IncorrectQuotingException

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

    @Test
    fun doubleQuotesInterpolation() {
        val result = InterpolationLexer.tokenize("cat \"exam\"ple.txt")
        val expectedResult = listOf("cat", "example.txt")
        assertEquals(expectedResult, result)
    }

    @Test
    fun singleQuotesInterpolation() {
        val result = InterpolationLexer.tokenize("cat 'exam'ple.txt")
        val expectedResult = listOf("cat", "example.txt")
        assertEquals(expectedResult, result)
    }

    @Test(expected = IncorrectQuotingException::class)
    fun incorrectQuotingThrowsExceptionDifferentQuoting() {
        InterpolationLexer.tokenize("cat 'exam\"ple.txt")
    }

    @Test(expected = IncorrectQuotingException::class)
    fun incorrectQuotingThrowsExceptionOneTypeQuoting() {
        InterpolationLexer.tokenize("echo \"text")
    }

    @Test
    fun pipelineWithoutSpace() {
        val result = InterpolationLexer.tokenize("echo \"some text\"|wc")
        val expectedResult = listOf("echo", "some text", "|", "wc")
        assertEquals(expectedResult, result)
    }

    @Test
    fun correctInterpolationWithTwoVariables() {
        GlobalEnvironment.setVariable("a", "ec")
        GlobalEnvironment.setVariable("b", "ho")
        val result = InterpolationLexer.tokenize("\$a\$b text")
        val expectedResult = listOf("echo", "text")
        assertEquals(expectedResult, result)
    }

    @Test
    fun emptyCommandInPipeline() {
        val result = InterpolationLexer.tokenize("echo text |")
        val expectedResult = listOf("echo", "text", "|")
        assertEquals(expectedResult, result)
    }

    @Test
    fun emptyCommandInPipelineWithSpace() {
        val result = InterpolationLexer.tokenize("echo text | ")
        val expectedResult = listOf("echo", "text", "|")
        assertEquals(expectedResult, result)
    }

    @Test
    fun singleQuotesInDoubleQuotes() {
        GlobalEnvironment.setVariable("t", "text")
        val result = InterpolationLexer.tokenize("echo \"'\$t'\"")
        val expectedResult = listOf("echo", "'text'")
        assertEquals(expectedResult, result)
    }

    @Test
    fun pipeInSingeQuotes() {
        GlobalEnvironment.setVariable("t", "text")
        val result = InterpolationLexer.tokenize("echo \"'\$t'\"")
        val expectedResult = listOf("echo", "'text'")
        assertEquals(expectedResult, result)
    }

    @Test
    fun doubleQuotesInAssignment() {
        val result = InterpolationLexer.tokenize("\"\"=text")
        val expectedResult = listOf("=text")
        assertEquals(expectedResult, result)
    }
}
package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test

class EchoTest {
    @Test
    fun echoNoArgument() {
        val expected = "\n"
        assertEquals(expected, Echo(listOf(), null).execute())
    }

    @Test
    fun echoOneArgument() {
        val expected = "text\n"
        assertEquals(expected, Echo(listOf("text"), null).execute())
    }

    @Test
    fun echoTwoArguments() {
        val arg1 = "arg1"
        val arg2 = "arg2"
        val expected = "arg1 arg2\n"
        assertEquals(expected, Echo(listOf(arg1, arg2), null).execute())
    }

    @Test
    fun echoIgnoresPipeline() {
        val arg1 = "arg1"
        val arg2 = "arg2"
        val expected = "arg1\n"
        assertEquals(expected, Echo(listOf(arg1), Echo(listOf(arg2), null)).execute())
    }
}
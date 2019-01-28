package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test

class UnknownCommandTest {
    @Test
    fun noArguments() {
        val expected = System.lineSeparator()
        assertEquals(expected, UnknownCommand("echo", listOf(), null).execute())
    }

    @Test
    fun oneArgument() {
        val expected = "text${System.lineSeparator()}"
        assertEquals(expected, UnknownCommand("echo", listOf("text"), null).execute())
    }

    @Test
    fun twoArguments() {
        val expected = "text1 text2${System.lineSeparator()}"
        assertEquals(expected, UnknownCommand("echo", listOf("text1", "text2"), null).execute())
    }

    @Test
    fun pipelineArguments() {
        val expected = "text${System.lineSeparator()}"
        assertEquals(expected, UnknownCommand("echo", listOf(), Echo(listOf("text"), null)).execute())
    }

    @Test
    fun argumentsHaveGreaterPriorityThanPipelines() {
        val expected = "argument text${System.lineSeparator()}"
        assertEquals(expected, UnknownCommand("echo", listOf("argument text"),
            Echo(listOf("pipeline text"), null)).execute())
    }
}
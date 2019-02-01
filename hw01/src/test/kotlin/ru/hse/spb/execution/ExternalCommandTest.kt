package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.exceptions.ExternalCommandException

class ExternalCommandTest {
    @Test
    fun noArguments() {
        val expected = System.lineSeparator()
        assertEquals(expected, ExternalCommand("echo", listOf(), null).execute())
    }

    @Test
    fun oneArgument() {
        val expected = "text" + System.lineSeparator()
        assertEquals(expected, ExternalCommand("echo", listOf("text"), null).execute())
    }

    @Test
    fun twoArguments() {
        val expected = "text1 text2" + System.lineSeparator()
        assertEquals(expected, ExternalCommand("echo", listOf("text1", "text2"), null).execute())
    }

    @Test
    fun pipelineArguments() {
        // External command gets as input a content of pipeline, not a file name of it. That is why content is expected.
        val expected = "text" + System.lineSeparator() + System.lineSeparator()
        assertEquals(
            expected, ExternalCommand(
                "echo", listOf(),
                Echo(listOf("text"), null)
            ).execute()
        )
    }

    @Test
    fun argumentsHaveGreaterPriorityThanPipelines() {
        val expected = "argument text" + System.lineSeparator()
        assertEquals(expected, ExternalCommand("echo", listOf("argument text"),
            Echo(listOf("pipeline text"), null)).execute())
    }

    @Test(expected = ExternalCommandException::class)
    fun externalCommandThrowsException() {
        ExternalCommand("nosuchcommand_ru.hse.spb", listOf("argument text"), null).execute()
    }
}
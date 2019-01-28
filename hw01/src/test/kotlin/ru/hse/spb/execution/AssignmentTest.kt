package ru.hse.spb.execution


import junit.framework.TestCase.assertEquals
import org.junit.Test

class AssignmentTest {

    @Test
    fun execute() {
    }

    @Test
    fun getCommandName() {
        assertEquals("=", Assignment("", "", null).getCommandName())
    }
}
package ru.hse.spb.environment

import junit.framework.TestCase.assertEquals
import org.junit.Test

class GlobalEnvironmentTest {
    @Test
    fun returnsNull() {
        assertEquals(null, GlobalEnvironment.getValue(""))
    }

    @Test
    fun returnsValueThatWasSet() {
        GlobalEnvironment.setVariable("var", "val")
        assertEquals("val", GlobalEnvironment.getValue("var"))
    }

    @Test
    fun returnsValueThatWasSetTwice() {
        GlobalEnvironment.setVariable("var", "val1")
        assertEquals("val1", GlobalEnvironment.getValue("var"))
        GlobalEnvironment.setVariable("var", "val2")
        assertEquals("val2", GlobalEnvironment.getValue("var"))
    }

    @Test
    fun combinedTest() {
        GlobalEnvironment.setVariable("var", "val1")
        assertEquals("val1", GlobalEnvironment.getValue("var"))
        GlobalEnvironment.setVariable("var", "val")
        assertEquals("val", GlobalEnvironment.getValue("var"))
        GlobalEnvironment.setVariable("var", "val2")
        assertEquals("val2", GlobalEnvironment.getValue("var"))
    }
}
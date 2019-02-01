package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.environment.GlobalEnvironment

class AssignmentTest {
    @Test
    fun assignOneValue() {
        assertEquals(
            "", Assignment(
                "var", "val",
                true, null
            ).execute()
        )
        assertEquals("val", GlobalEnvironment.getValue("var"))
    }

    @Test
    fun assignVariableTwice() {
        assertEquals(
            "", Assignment(
                "var", "val1",
                true, null
            ).execute()
        )
        assertEquals("val1", GlobalEnvironment.getValue("var"))
        assertEquals(
            "", Assignment(
                "var", "val2",
                true, null
            ).execute()
        )
        assertEquals("val2", GlobalEnvironment.getValue("var"))
    }

    @Test
    fun assignTwoVariables() {
        assertEquals(
            "", Assignment(
                "var1", "val1",
                true, null
            ).execute()
        )
        assertEquals("val1", GlobalEnvironment.getValue("var1"))
        assertEquals(
            "", Assignment(
                "var2", "val2",
                true, null
            ).execute()
        )
        assertEquals("val1", GlobalEnvironment.getValue("var1"))
        assertEquals("val2", GlobalEnvironment.getValue("var2"))
    }

    @Test
    fun assignViaPipelineDoesNotWork() {
        val assignment = Assignment(
            "var2", "val2", false,
            Assignment(
                "var2", "val1", false,
                Assignment("var1", "val1", false, null)
            )
        )
        assertEquals("", assignment.execute())
        assertEquals(null, GlobalEnvironment.getValue("var1"))
        assertEquals(null, GlobalEnvironment.getValue("var2"))
    }
}
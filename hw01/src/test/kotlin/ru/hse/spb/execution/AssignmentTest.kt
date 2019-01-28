package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.environment.GlobalEnvironment

class AssignmentTest {
    @Test
    fun assignOneValue() {
        assertEquals(null, Assignment("var", "val",
            false,null).execute())
        assertEquals("val", GlobalEnvironment.getValue("var"))
    }

    @Test
    fun assignVariableTwice() {
        assertEquals(null, Assignment("var", "val1",
            false,null).execute())
        assertEquals("val1", GlobalEnvironment.getValue("var"))
        assertEquals(null, Assignment("var", "val2",
            false,null).execute())
        assertEquals("val2", GlobalEnvironment.getValue("var"))
    }

    @Test
    fun assignTwoVariables() {
        assertEquals(null, Assignment("var1", "val1",
            false,null).execute())
        assertEquals("val1", GlobalEnvironment.getValue("var1"))
        assertEquals(null, Assignment("var2", "val2",
            false,null).execute())
        assertEquals("val1", GlobalEnvironment.getValue("var1"))
        assertEquals("val2", GlobalEnvironment.getValue("var2"))
    }

    @Test
    fun assignViaPipelineDoesNotWork() {
        val assignment = Assignment("var2", "val2", true,
            Assignment("var2", "val1", true,
                Assignment("var1", "val1", true, null)))
        assertEquals(null, assignment.execute())
        assertEquals(null, GlobalEnvironment.getValue("var1"))
        assertEquals(null, GlobalEnvironment.getValue("var2"))
    }
}
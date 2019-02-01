package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test

class ExitTest {
    @Test
    fun exitNotAppliedInPipeline() {
        assertEquals("", Exit(false, Exit(false, null)).execute())
    }
}
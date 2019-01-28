package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.ExpectedSystemExit
import java.nio.file.Paths
import org.junit.rules.ExpectedException.none

class ExitTest {
    @Test
    fun exitNotAppliedInPipeline() {
        assertEquals(null, Exit(true, Exit(true, null)).execute())
    }
}
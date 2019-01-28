package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.ExpectedSystemExit
import java.nio.file.Paths
import org.junit.rules.ExpectedException.none
import kotlin.math.exp

class PwdTest {
    @Test
    fun pwdWithoutPipeline() {
        val expected = Paths.get("").toAbsolutePath().toString()
        assertEquals(expected, Pwd(null).execute())
    }

    @Test
    fun pwdInPipelineTwoTimes() {
        val expected = Paths.get("").toAbsolutePath().toString()
        assertEquals(expected, Pwd(Pwd(null)).execute())
    }

    @Test
    fun pwdInPipelineThreeTimes() {
        val expected = Paths.get("").toAbsolutePath().toString()
        assertEquals(expected, Pwd(Pwd(Pwd(null))).execute())
    }
}
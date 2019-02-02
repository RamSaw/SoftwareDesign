package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import java.nio.file.Paths

class PwdTest {
    @Test
    fun pwdWithoutPipeline() {
        val expected = Paths.get("").toAbsolutePath().toString() + "\n"
        assertEquals(expected, Pwd(null).execute())
    }

    @Test
    fun pwdInPipelineTwoTimes() {
        val expected = Paths.get("").toAbsolutePath().toString() + "\n"
        assertEquals(expected, Pwd(Pwd(null)).execute())
    }

    @Test
    fun pwdInPipelineThreeTimes() {
        val expected = Paths.get("").toAbsolutePath().toString() + "\n"
        assertEquals(expected, Pwd(Pwd(Pwd(null))).execute())
    }
}
package ru.hse.spb.execution

import org.junit.Assert.assertTrue
import org.junit.Test
import ru.hse.spb.exceptions.ExternalCommandException

class ExternalCommandTest {
    @Test
    fun externalCommandGit() {
        val expectedBeginning = "git version"
        assertTrue(
            ExternalCommand("git", listOf("--version"), null).execute().startsWith(expectedBeginning)
        )
    }

    @Test(expected = ExternalCommandException::class)
    fun externalCommandThrowsException() {
        ExternalCommand("nosuchcommand_ru.hse.spb", listOf("argument text"), null).execute()
    }
}
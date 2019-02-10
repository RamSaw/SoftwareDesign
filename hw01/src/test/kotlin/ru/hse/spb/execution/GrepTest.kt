package ru.hse.spb.execution

import junit.framework.TestCase.assertEquals
import org.junit.Test
import ru.hse.spb.getResourceFilePath

class GrepTest {
    @Test
    fun grepNoArgument() {
        val expected = ""
        assertEquals(expected, Grep(Regex(""), null, null).execute())
    }

    @Test
    fun grepNoMatches() {
        val expected = ""
        assertEquals(
            expected,
            Grep(Regex("NoMatch"), null, Echo(listOf("No matches\nNo matches"), null)).execute()
        )
    }

    @Test
    fun basicGrepFromPipeline() {
        val testFilePath = getResourceFilePath("testFile1")
        val expected = "contentOfTestFile1\n"
        assertEquals(expected, Grep(Regex("TestFile"), testFilePath, null).execute())
    }

    @Test
    fun basicGrepFromFile() {
        val text = "Matches this line\nNot matches this line\nMatchesthisline"
        val expected = "Matches this line\nMatchesthisline\n"
        assertEquals(
            expected,
            Grep(Regex("Matches"), null, Echo(listOf(text), null)).execute()
        )
    }

    @Test
    fun grep2LinesAfter() {
        val text = "Matches\nOne more line\nMatches\nTwo more lines\nTwo more lines\nNo line in output"
        val expected = "Matches\nOne more line\nMatches\nTwo more lines\nTwo more lines\n"
        assertEquals(
            expected,
            Grep(Regex("Match"), null, Echo(listOf(text), null), 2).execute()
        )
    }

    @Test
    fun grep3LinesAfterButInputEnds() {
        val text = "Matches\nOne more line\nMatches\nThree more lines but text ends"
        val expected = "Matches\nOne more line\nMatches\nThree more lines but text ends\n" + "\n" // \n from Echo
        assertEquals(
            expected,
            Grep(Regex("Match"), null, Echo(listOf(text), null), 3).execute()
        )
    }

    @Test
    fun grepWordsNotMatching() {
        val text = "Matches bla bla"
        val expected = ""
        assertEquals(
            expected,
            Grep(
                Regex("Match"), null,
                Echo(listOf(text), null), 0, true
            ).execute()
        )
    }

    @Test
    fun grepWordsBeginning() {
        val text = "Matches bla bla"
        val expected = "Matches bla bla\n"
        assertEquals(
            expected,
            Grep(
                Regex("Matches"), null,
                Echo(listOf(text), null), 0, true
            ).execute()
        )
    }

    @Test
    fun grepWordsEnd() {
        val text = "bla bla Matches"
        val expected = "bla bla Matches\n"
        assertEquals(
            expected,
            Grep(
                Regex("Matches"), null,
                Echo(listOf(text), null), 0, true
            ).execute()
        )
    }

    @Test
    fun grepWordsMiddle() {
        val text = "bla Matches bla"
        val expected = "bla Matches bla\n"
        assertEquals(
            expected,
            Grep(
                Regex("Matches"), null,
                Echo(listOf(text), null), 0, true
            ).execute()
        )
    }
}
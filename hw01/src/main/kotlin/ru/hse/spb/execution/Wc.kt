package ru.hse.spb.execution

import java.nio.file.Path
import java.util.*

/**
 * Implementation of wc command: wc <path to file1> <path to file2> ... .
 * The output for each file: <number of rows> <number of words> <number of bytes>.
 * If more than one file path is passed then total metrics are counted too.
 */
class Wc(arguments: List<Path>, prev: Executable?) :
    OneTypeArgumentsExecutable<Path>(arguments, prev) {
    override fun processArgumentsInput(): String {
        var totalNumberOfRows = 0
        var totalNumberOfWords = 0
        var totalNumberOfBytes = 0
        val result = arguments.joinToString(System.lineSeparator()) { path ->
            val rows = path.toFile().readLines().size
            val words = StringTokenizer(path.toFile().readText()).countTokens()
            val bytes = path.toFile().readBytes().size + 1
            totalNumberOfRows += rows
            totalNumberOfWords += words
            totalNumberOfBytes += bytes
            "$rows $words $bytes ${path.fileName}"
        }
        return when (arguments.size) {
            0 -> "0 0 0"
            1 -> result
            else -> "$result\n$totalNumberOfRows $totalNumberOfWords $totalNumberOfBytes total"
        }
    }

    override fun processPipelineInput(pipeLineInput: String?) =
        pipeLineInput?.let {"${it.lines().size} ${StringTokenizer(it).countTokens()} ${it.toByteArray().size + 1}"}
            ?: "0 0 0"
}
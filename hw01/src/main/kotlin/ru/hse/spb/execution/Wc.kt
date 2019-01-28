package ru.hse.spb.execution

import java.nio.file.Path
import java.util.*

class Wc(arguments: List<Path>, prev: Executable?) :
    OneTypeArgumentsExecutable<Path>(arguments, prev) {
    override fun getCommandName(): String {
        return "wc"
    }

    override fun executeWithArguments(): String {
        var totalNumberOfRows = 0
        var totalNumberOfWords = 0
        var totalNumberOfBytes = 0
        return arguments.joinToString("\n") { path ->
            val rows = path.toFile().readLines().size
            val words = StringTokenizer(path.toFile().readText()).countTokens()
            val bytes = path.toFile().readBytes().size
            totalNumberOfRows += rows
            totalNumberOfWords += words
            totalNumberOfBytes += bytes
            "$rows $words $bytes"
        } + "\n $totalNumberOfRows $totalNumberOfWords $totalNumberOfBytes"
    }
}
package ru.hse.spb.execution

import ru.hse.spb.pipeline.Pipeline
import java.nio.file.Path
import java.util.*

/**
 * Implementation of wc command: wc <path to file1> <path to file2> ... .
 * The output for each file: <number of rows - 1> <number of words> <number of bytes>.
 * If more than one file path is passed then total metrics are counted too.
 */
class Wc(arguments: List<Path>, prev: Executable?) :
    OneTypeArgumentsExecutable<Path>(arguments, prev) {

    override fun processArgumentsInput(): String {
        var totalNumberOfRows = 0
        var totalNumberOfWords = 0
        var totalNumberOfBytes = 0
        val result = arguments.joinToString("\n") { path ->
            val rows = getRowsNumberForBashLikeCounting(path)
            val words = StringTokenizer(path.toFile().readText()).countTokens()
            val bytes = path.toFile().readBytes().size
            totalNumberOfRows += rows
            totalNumberOfWords += words
            totalNumberOfBytes += bytes
            "$rows $words $bytes ${path.fileName}"
        }
        return when (arguments.size) {
            1 -> result
            else -> "$result\n$totalNumberOfRows $totalNumberOfWords $totalNumberOfBytes total"
        }
    }

    override fun processPipelineInput(pipeLine: Pipeline) =
        pipeLine.getContent().let {
            "${getRowsNumberForBashLikeCounting(it)} " +
                    "${StringTokenizer(it).countTokens()} ${it.toByteArray().size}"
        }

    override fun processEmptyInput() = "0 0 0"

    private fun getRowsNumberForBashLikeCounting(filePath: Path) =
        getRowsNumberForBashLikeCounting(filePath.toFile().readText())

    private fun getRowsNumberForBashLikeCounting(fileContent: String) = fileContent.lines().size - 1
}
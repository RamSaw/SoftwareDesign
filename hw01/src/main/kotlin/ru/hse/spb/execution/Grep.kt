package ru.hse.spb.execution

import ru.hse.spb.pipeline.Pipeline
import java.nio.file.Path

class Grep(
    private val regex: Regex, private val filePath: Path?,
    prev: Executable?, private val linesToPrintAfter: Int = 0,
    private val wordRegexp: Boolean = false
) : PipelineExecutable(prev) {
    companion object {
        private val separator = "--"
    }

    override fun hasArguments() = filePath != null

    override fun processArgumentsInput() = grepLines(filePath!!.toFile().readLines())

    override fun processPipelineInput(pipeLine: Pipeline) = grepLines(pipeLine.getContent().lines())

    override fun processEmptyInput() = ""

    private fun grepLines(lines: List<String>): String {
        var linesToPrint = 0
        val result = StringBuilder()
        for (line in lines) {
            if (lineMatches(line)) {
                if (linesToPrintAfter > 0 && linesToPrint == 0 && result.isNotEmpty()) {
                    result.append("$separator\n")
                }
                linesToPrint = linesToPrintAfter
                result.append(line).append("\n")
            } else if (linesToPrint > 0) {
                linesToPrint--
                result.append(line).append("\n")
            }
        }
        return result.toString()
    }

    private fun lineMatches(line: String) =
        regex.findAll(line).any { matchResult -> !wordRegexp || isWords(line, matchResult.range) }

    private fun isWords(line: String, range: IntRange) =
        (range.first == 0 || !isWordConstituentCharacter(line[range.first - 1])) &&
                (range.endInclusive == line.lastIndex || !isWordConstituentCharacter(line[range.endInclusive + 1]))

    private fun isWordConstituentCharacter(c: Char) = c.isLetterOrDigit() || c == '_'
}
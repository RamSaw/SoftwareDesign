package ru.hse.spb.execution

import java.nio.file.Path

/**
 * Implementation of cat command: cat <filepath1> <filepath2> ... .
 * Returns: <content of file1>\n<content of file2> ... .
 */
class Cat(arguments: List<Path>, prev: Executable?) :
    OneTypeArgumentsExecutable<Path>(arguments, prev) {
    override fun processArgumentsInput() =
        arguments.joinToString(System.lineSeparator()) { path ->  path.toFile().readText() }

    override fun processPipelineInput(pipeLineInput: String?) = pipeLineInput.orEmpty()
}
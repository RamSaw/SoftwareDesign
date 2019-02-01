package ru.hse.spb.execution

import ru.hse.spb.pipeline.Pipeline

/**
 * Implementation of echo command: echo <str1> <str2> ... .
 * Returns: <str1> <str2> ... .
 * Note: pipelines are ignored by this command
 */
class Echo(arguments: List<String>, prev: Executable?) :
    OneTypeArgumentsExecutable<String>(arguments, prev) {
    override fun processEmptyInput(): String = System.lineSeparator()

    override fun processArgumentsInput() = arguments.joinToString(" ") + System.lineSeparator()

    override fun processPipelineInput(pipeLine: Pipeline): String = pipeLine.getFileName() + System.lineSeparator()
}
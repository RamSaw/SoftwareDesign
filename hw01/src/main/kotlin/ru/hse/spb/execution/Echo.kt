package ru.hse.spb.execution

/**
 * Implementation of echo command: echo <str1> <str2> ... .
 * Returns: <str1> <str2> ... .
 * Note: pipelines are ignored by this command
 */
class Echo(arguments: List<String>, prev: Executable?) :
    OneTypeArgumentsExecutable<String>(arguments, prev) {
    override fun processArgumentsInput() = arguments.joinToString(" ")

    override fun processPipelineInput(pipeLineInput: String?) = ""
}
package ru.hse.spb.execution

import ru.hse.spb.lexer.InterpolationLexer
import ru.hse.spb.parser.ExecutionParser

abstract class PipelineExecutable(private val prev: Executable?): Executable

abstract class NoArgumentsExecutable(private val prev: Executable?): Executable {
    override fun execute(): String {
        prev?.execute()
        return executeWithoutArguments()
    }

    abstract fun executeWithoutArguments(): String

}

abstract class OneTypeArgumentsExecutable<T>(protected val arguments: List<T>,
                                             private val prev: Executable?): PipelineExecutable(prev) {
    override fun execute(): String? {
        val prevResult = prev?.execute()
        return if (arguments.isEmpty() && prevResult != null) {
            return ExecutionParser.parse(listOf(getCommandName()) +
                    InterpolationLexer.tokenize(prevResult)).execute()
        } else {
            executeWithArguments()
        }
    }

    abstract fun executeWithArguments(): String
}
package ru.hse.spb.execution

/**
 * Describes executable that can be used in pipeline.
 * As parameter constructor takes previous command to run.
 */
abstract class PipelineExecutable(protected val prev: Executable?): Executable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PipelineExecutable

        if (prev != other.prev) return false

        return true
    }

    override fun hashCode(): Int {
        return prev?.hashCode() ?: 0
    }
}

/**
 * Implementation of execute method for commands that do not take any arguments.
 */
abstract class NoArgumentsExecutable(prev: Executable?) : PipelineExecutable(prev) {
    override fun execute(): String? {
        prev?.execute()
        return executeWithoutArguments()
    }

    protected abstract fun executeWithoutArguments(): String?
}

/**
 * Implementation of execute method for commands that take arguments with one specific type.
 */
abstract class OneTypeArgumentsExecutable<T>(protected val arguments: List<T>,
                                             prev: Executable?): PipelineExecutable(prev) {
    override fun execute(): String? {
        return executeWithArguments(prev?.execute())
    }

    private fun executeWithArguments(pipeLineInput: String?): String {
        return if (!arguments.isEmpty())
            processArgumentsInput()
        else
            processPipelineInput(pipeLineInput)
    }

    protected abstract fun processArgumentsInput(): String

    protected abstract fun processPipelineInput(pipeLineInput: String?): String

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as OneTypeArgumentsExecutable<*>

        if (arguments != other.arguments) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + arguments.hashCode()
        return result
    }
}
package ru.hse.spb.execution

import ru.hse.spb.environment.GlobalEnvironment

/**
 * Implementation of assignment command: var=val.
 * This command can be used in pipeline but returns null.
 */
class Assignment(private val variableName: String,
                 private val variableValue: String,
                 private val isInPipeline: Boolean,
                 prev: Executable?) : PipelineExecutable(prev) {
    override fun execute(): String? {
        if (!isInPipeline) {
            GlobalEnvironment.setVariable(variableName, variableValue)
        }
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Assignment

        if (variableName != other.variableName) return false
        if (variableValue != other.variableValue) return false
        if (isInPipeline != other.isInPipeline) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + variableName.hashCode()
        result = 31 * result + variableValue.hashCode()
        result = 31 * result + isInPipeline.hashCode()
        return result
    }
}
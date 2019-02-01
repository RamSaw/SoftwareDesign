package ru.hse.spb.execution

import ru.hse.spb.environment.GlobalEnvironment
import ru.hse.spb.pipeline.Pipeline

/**
 * Implementation of assignment command: var=val.
 * This command can be used in pipeline but returns empty string.
 */
class Assignment(private val variableName: String,
                 private val variableValue: String,
                 private val isSingleInPipeline: Boolean,
                 prev: Executable?) : PipelineExecutable(prev) {
    override fun executeWithPipeline(pipeLine: Pipeline?): String {
        if (isSingleInPipeline) {
            GlobalEnvironment.setVariable(variableName, variableValue)
        }
        return ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Assignment

        if (variableName != other.variableName) return false
        if (variableValue != other.variableValue) return false
        if (isSingleInPipeline != other.isSingleInPipeline) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + variableName.hashCode()
        result = 31 * result + variableValue.hashCode()
        result = 31 * result + isSingleInPipeline.hashCode()
        return result
    }
}
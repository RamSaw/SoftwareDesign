package ru.hse.spb.execution

import ru.hse.spb.environment.GlobalEnvironment

class Assignment(private val variableName: String,
                 private val variableValue: String,
                 prev: Executable?) : PipelineExecutable(prev) {
    override fun execute(): String? {
        GlobalEnvironment.setVariable(variableName, variableValue)
        return null
    }

    override fun getCommandName(): String {
        return "="
    }
}
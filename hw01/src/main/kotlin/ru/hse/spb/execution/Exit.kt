package ru.hse.spb.execution

import kotlin.system.exitProcess

/**
 * Implementation of exit command: just exits the program
 */
class Exit(private val isInPipeline: Boolean, prev: Executable?) : NoArgumentsExecutable(prev) {
    override fun executeWithoutArguments(): String? {
        if (!isInPipeline)
            exitProcess(0)
        return null
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Exit

        if (isInPipeline != other.isInPipeline) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + isInPipeline.hashCode()
        return result
    }
}
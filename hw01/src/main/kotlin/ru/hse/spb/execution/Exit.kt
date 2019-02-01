package ru.hse.spb.execution

import ru.hse.spb.pipeline.Pipeline
import kotlin.system.exitProcess

/**
 * Implementation of exit command: just exits the program
 */
class Exit(private val isSingleInPipeline: Boolean, prev: Executable?) : NoArgumentsExecutable(prev) {
    override fun executeWithPipeline(pipeLine: Pipeline?): String {
        if (isSingleInPipeline)
            exitProcess(0)
        return ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Exit

        if (isSingleInPipeline != other.isSingleInPipeline) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + isSingleInPipeline.hashCode()
        return result
    }
}
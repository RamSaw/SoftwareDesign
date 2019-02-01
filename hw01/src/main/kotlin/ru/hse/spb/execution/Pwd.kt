package ru.hse.spb.execution

import ru.hse.spb.pipeline.Pipeline
import java.nio.file.Paths

/**
 * Implementation of pwd command: takes no arguments and returns current directory
 */
class Pwd(prev: Executable?) : NoArgumentsExecutable(prev) {
    override fun executeWithPipeline(pipeLine: Pipeline?): String {
        return Paths.get("").toAbsolutePath().toString()
    }
}
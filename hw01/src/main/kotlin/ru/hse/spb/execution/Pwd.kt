package ru.hse.spb.execution

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Implementation of pwd command: takes no arguments and returns current directory
 */
class Pwd(prev: Executable?) : NoArgumentsExecutable(prev) {
    override fun executeWithoutArguments(): String {
        return Paths.get("").toAbsolutePath().toString()
    }
}
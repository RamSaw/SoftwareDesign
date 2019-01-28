package ru.hse.spb.execution

import java.nio.file.Paths


class Pwd(prev: Executable?) :
    NoArgumentsExecutable(prev) {
    override fun getCommandName(): String {
        return "pwd"
    }

    override fun executeWithoutArguments(): String {
        return Paths.get("").toAbsolutePath().toString()
    }
}
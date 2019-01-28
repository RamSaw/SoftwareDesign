package ru.hse.spb.execution

import java.nio.file.Path


class Cat(arguments: List<Path>, prev: Executable?) :
    OneTypeArgumentsExecutable<Path>(arguments, prev) {
    override fun getCommandName(): String {
        return "cat"
    }

    override fun executeWithArguments(): String {
        return arguments.joinToString("\n") { path ->  path.toFile().readText() }
    }
}
package ru.hse.spb.execution

import kotlin.system.exitProcess

class Exit(prev: Executable?) :
    NoArgumentsExecutable(prev) {
    override fun getCommandName(): String {
        return "exit"
    }

    override fun executeWithoutArguments(): String {
        exitProcess(0)
    }
}
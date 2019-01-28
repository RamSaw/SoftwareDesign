package ru.hse.spb.execution

interface Executable {
    fun execute(): String?
    fun getCommandName(): String
}
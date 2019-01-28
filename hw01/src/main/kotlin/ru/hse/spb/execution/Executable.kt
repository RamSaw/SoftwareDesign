package ru.hse.spb.execution

/**
 * Base interface to run CLI commands.
 */
interface Executable {
    /**
     * Executes command
     * @return string result or null if command has no result
     */
    fun execute(): String?
}
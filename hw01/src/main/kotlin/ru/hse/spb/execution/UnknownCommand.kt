package ru.hse.spb.execution

import ru.hse.spb.exceptions.UnknownCommandException
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

/**
 * Implementation of running external program if the command is unknown.
 * Pipeline input is passed as one argument.
 */
class UnknownCommand(private val commandName: String, arguments: List<String>, prev: Executable?):
    OneTypeArgumentsExecutable<String>(arguments, prev) {
    override fun processArgumentsInput() =
        runCommand(listOf(commandName) + arguments, Paths.get("").toAbsolutePath().toFile())

    override fun processPipelineInput(pipeLineInput: String?) =
        runCommand(listOfNotNull(commandName, pipeLineInput), Paths.get("").toAbsolutePath().toFile())

    private fun runCommand(commandWithArguments: List<String>, workingDir: File): String {
        return try {
            val proc = ProcessBuilder(*commandWithArguments.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(60, TimeUnit.MINUTES)
            proc.inputStream.bufferedReader().readText()
        } catch(e: IOException) {
            throw UnknownCommandException("External command ${commandWithArguments[0]} went to an error: " +
                    "${e.message}. Maybe wrong name of command?")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as UnknownCommand

        if (commandName != other.commandName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + commandName.hashCode()
        return result
    }
}
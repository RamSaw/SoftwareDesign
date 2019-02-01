package ru.hse.spb.execution

import ru.hse.spb.exceptions.ExternalCommandException
import ru.hse.spb.pipeline.Pipeline
import java.io.File
import java.io.IOException
import java.nio.file.Paths
import java.util.concurrent.TimeUnit

/**
 * Implementation of running external program if the command is unknown.
 * Pipeline input is passed as one argument.
 */
class ExternalCommand(private val commandName: String, arguments: List<String>, prev: Executable?):
    OneTypeArgumentsExecutable<String>(arguments, prev) {
    override fun processEmptyInput() =
        runCommand(listOf(commandName), getCurrentDir())

    override fun processArgumentsInput() =
        runCommand(listOf(commandName) + arguments, getCurrentDir())

    override fun processPipelineInput(pipeLine: Pipeline) =
        runCommand(listOfNotNull(commandName, pipeLine.getContent()), getCurrentDir())

    private fun getCurrentDir() = Paths.get("").toAbsolutePath().toFile()

    private fun runCommand(commandWithArguments: List<String>, workingDir: File): String {
        return try {
            val proc = ProcessBuilder(*commandWithArguments.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

            proc.waitFor(1, TimeUnit.SECONDS)
            proc.inputStream.bufferedReader().readText()
        } catch(e: IOException) {
            throw ExternalCommandException("External command ${commandWithArguments[0]} went to an error: " +
                    "${e.message}. Maybe wrong name of command?")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ExternalCommand

        if (commandName != other.commandName) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + commandName.hashCode()
        return result
    }
}
package ru.hse.spb.parser

import ru.hse.spb.exceptions.UnknownCommandException
import ru.hse.spb.exceptions.WrongCommandArgumentsException
import ru.hse.spb.execution.*
import java.nio.file.Paths

/**
 * Implementation of parser that supports pipeline and creates executables
 */
object ExecutionParser: Parser {
    override fun parse(tokens: List<String>): Executable {
        val partitionedTokens = partition(tokens)
        var result: Executable? = null
        val isSingleCommandInPipelineTokens = isSingleInPipeline(partitionedTokens)
        for (command in partitionedTokens) {
            if (command.isEmpty()) {
                throw UnknownCommandException("Unknown command: command is empty")
            }
            result = when (command[0]) {
                "=" -> {
                    if (command.size != 3) {
                        throw WrongCommandArgumentsException("Wrong arguments: correct syntax for assignment <name>=<value>")
                    }
                    Assignment(command[1], command[2], isSingleCommandInPipelineTokens, result)
                }
                "cat" -> Cat(command.subList(1, command.size).map { s -> Paths.get(s) }, result)
                "exit" -> Exit(isSingleCommandInPipelineTokens, result)
                "pwd" -> Pwd(result)
                "wc" -> Wc(command.subList(1, command.size).map { s -> Paths.get(s) }, result)
                "echo" -> Echo(command.subList(1, command.size), result)
                else -> ExternalCommand(command[0], command.subList(1, command.size), result)
            }
        }
        return result ?: object : Executable {
            override fun execute(): String {
                return System.lineSeparator()
            }
        }
    }

    private fun isSingleInPipeline(partitionedTokens: List<List<String>>): Boolean {
        return partitionedTokens.size == 1
    }

    private fun partition(tokens: List<String>): List<List<String>> {
        val result = mutableListOf<List<String>>()
        var currentCommand = mutableListOf<String>()
        for (token in tokens) {
            if (token != "|") {
                currentCommand.add(token)
            } else {
                result.add(currentCommand)
                currentCommand = mutableListOf()
            }
        }
        result.add(currentCommand)
        return result
    }
}
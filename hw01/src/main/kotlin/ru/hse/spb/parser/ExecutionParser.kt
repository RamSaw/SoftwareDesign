package ru.hse.spb.parser

import ru.hse.spb.exceptions.UnknownCommandException
import ru.hse.spb.exceptions.WrongCommandArguments
import ru.hse.spb.execution.*
import java.nio.file.Paths

object ExecutionParser: Parser {
    override fun parse(tokens: List<String>): Executable {
        val partitionedTokens = partition(tokens)
        var result: Executable? = null
        for (command in partitionedTokens) {
            if (command.isEmpty()) {
                throw UnknownCommandException("Unknown command: command is empty")
            }
            result = when (command[0]) {
                "=" -> {
                    if (command.size != 3) {
                        throw WrongCommandArguments("Wrong arguments: correct syntax for assignment <name>=<value>")
                    }
                    Assignment(command[1], command[2], result)
                }
                "cat" -> Cat(command.subList(1, command.size).map { s -> Paths.get(s) }, result)
                "exit" -> Exit(result)
                "pwd" -> Pwd(result)
                "wc" -> Wc(command.subList(1, command.size).map { s -> Paths.get(s) }, result)
                else -> throw UnknownCommandException("Unknown command: ${command[0]}")
            }
        }
        return result ?: object : Executable {
            override fun execute(): String? {
                return ""
            }

            override fun getCommandName(): String {
                return ""
            }
        }
    }

    private fun partition(tokens: List<String>): List<List<String>> {
        val result = mutableListOf<List<String>>()
        val currentCommand = mutableListOf<String>()
        for (token in tokens) {
            if (token != "|") {
                currentCommand.add(token)
            } else {
                result.add(currentCommand)
                currentCommand.clear()
            }
        }
        return result
    }
}
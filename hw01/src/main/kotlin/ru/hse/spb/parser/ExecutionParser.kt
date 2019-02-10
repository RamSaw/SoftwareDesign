package ru.hse.spb.parser

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
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
            val arguments = command.subList(1, command.size)
            result = when (command[0]) {
                "=" -> {
                    if (command.size != 3) {
                        throw WrongCommandArgumentsException("Wrong arguments: correct syntax for assignment <name>=<value>")
                    }
                    Assignment(command[1], command[2], isSingleCommandInPipelineTokens, result)
                }
                "cat" -> Cat(arguments.map { s -> Paths.get(s) }, result)
                "exit" -> Exit(isSingleCommandInPipelineTokens, result)
                "pwd" -> Pwd(result)
                "wc" -> Wc(arguments.map { s -> Paths.get(s) }, result)
                "echo" -> Echo(command.subList(1, command.size), result)
                "grep" -> {
                    val grepArgumentsParser = object : CliktCommand() {
                        val ignoreCase by option("-i", "--ignore-case", help = "ignore case distinctions").flag()
                        val wordRegexp by option("-w", "--word-regexp", help = "match only words").flag()
                        val linesToPrintAfter: Int
                                by option(
                                    "-A", "--after-context",
                                    help = "Print NUM  lines  of  trailing  context  after  matching  lines"
                                )
                                    .int().restrictTo(0).default(0)
                        val regexString by argument()
                        val file by argument().file(
                            exists = true,
                            folderOkay = false,
                            readable = true
                        ).optional()

                        override fun run() {
                        }

                        fun getRegexp() =
                            if (ignoreCase) Regex(regexString, RegexOption.IGNORE_CASE) else Regex(regexString)
                    }
                    grepArgumentsParser.parse(arguments)
                    Grep(
                        grepArgumentsParser.getRegexp(), grepArgumentsParser.file?.toPath(), result,
                        grepArgumentsParser.linesToPrintAfter, grepArgumentsParser.wordRegexp
                    )
                }
                else -> ExternalCommand(command[0], command.subList(1, command.size), result)
            }
        }
        return result ?: object : Executable {
            override fun execute(): String {
                return "\n"
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
package ru.hse.spb

import ru.hse.spb.lexer.InterpolationLexer
import ru.hse.spb.parser.ExecutionParser

fun main(args: Array<String>) {
    CliApplication.run()
}

object CliApplication {
    fun run() {
        println("Welcome to CLI")
        while (true) {
            try {
                print(">")
                process(readLine()!!)?.apply { println(this) }
            } catch (e: Exception) {
                System.err.println(e.message)
            }
        }
    }

    fun process(input: String) = ExecutionParser.parse(InterpolationLexer.tokenize(input)).execute()
}
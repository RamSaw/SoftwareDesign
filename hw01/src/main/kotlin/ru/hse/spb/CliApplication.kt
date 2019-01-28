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
            print(">")
            println(process(readLine()!!))
        }
    }

    fun process(input: String) = ExecutionParser.parse(InterpolationLexer.tokenize(input)).execute().orEmpty()
}
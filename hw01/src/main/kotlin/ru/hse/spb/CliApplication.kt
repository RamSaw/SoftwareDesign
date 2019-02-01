package ru.hse.spb

import ru.hse.spb.lexer.InterpolationLexer
import ru.hse.spb.parser.ExecutionParser

fun main(args: Array<String>) {
    CliApplication.run()
}

/**
 * Main class of application.
 * Describes main cycle where user input is read and processed.
 */
object CliApplication {
    /**
     * Implements permanent loop to read and process user input.
     */
    fun run() {
        println("Welcome to CLI")
        while (true) {
            try {
                print(">")
                print(process(readLine()!!))
            } catch (e: Exception) {
                System.err.println(e.message)
            }
        }
    }

    /**
     * Processes user input and returns result of execution which can be null.
     */
    fun process(input: String) = ExecutionParser.parse(InterpolationLexer.tokenize(input)).execute()
}
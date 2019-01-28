package ru.hse.spb.parser

import ru.hse.spb.execution.Executable

/**
 * Interface to implement parser that translates tokens into executable
 */
interface Parser {
    /**
     * Method to parse tokens into executable
     */
    fun parse(tokens: List<String>): Executable
}
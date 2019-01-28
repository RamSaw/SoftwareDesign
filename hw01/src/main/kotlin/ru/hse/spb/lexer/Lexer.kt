package ru.hse.spb.lexer

/**
 * Lexer interface to split input string on tokens
 */
interface Lexer {
    /**
     * Converts input into list of tokens (words stored in type string)
     */
    fun tokenize(input: String): List<String>
}
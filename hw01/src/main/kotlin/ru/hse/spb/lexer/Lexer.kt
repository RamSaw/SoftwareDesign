package ru.hse.spb.lexer

interface Lexer {
    fun tokenize(input: String): List<String>
}
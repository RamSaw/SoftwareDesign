package ru.hse.spb.lexer

import ru.hse.spb.execution.Executable

interface Lexer {
    fun tokenize(input: String): List<String>
}
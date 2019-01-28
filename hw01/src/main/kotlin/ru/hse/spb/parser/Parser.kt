package ru.hse.spb.parser

import ru.hse.spb.execution.Executable

interface Parser {
    fun parse(tokens: List<String>): Executable
}
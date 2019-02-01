package ru.hse.spb.lexer

import ru.hse.spb.environment.GlobalEnvironment
import ru.hse.spb.exceptions.IncorrectQuotingException

/**
 * This implementation goes through string and substitutes all occurrences of $ and deletes quoting.
 * Also it partitions input on string tokens (words)
 */
object InterpolationLexer : Lexer {
    private var variableName: String? = null
    private var currentStringPart = ""
    private var state = State.NO_QUOTING

    private enum class State {
        DOUBLE_QUOTING, SINGLE_QUOTING, NO_QUOTING
    }

    /***
     * Tokenize an input and substitutes global variables
     * @return list of tokens
     */
    override fun tokenize(input: String): List<String> {
        clear()
        val result = mutableListOf<String>()

        for (ch in input) {
            when (ch) {
                '"' -> {
                    when (state) {
                        State.DOUBLE_QUOTING -> {
                            interpolateVariableName()
                            state = State.NO_QUOTING
                        }
                        State.SINGLE_QUOTING -> {
                            currentStringPart += ch
                        }
                        State.NO_QUOTING -> {
                            interpolateVariableName()
                            state = State.DOUBLE_QUOTING
                        }
                    }
                }
                '\'' -> {
                    when (state) {
                        State.DOUBLE_QUOTING -> {
                            interpolateVariableName()
                            currentStringPart += ch
                        }
                        State.SINGLE_QUOTING -> {
                            state = State.NO_QUOTING
                        }
                        State.NO_QUOTING -> {
                            interpolateVariableName()
                            state = State.SINGLE_QUOTING
                        }
                    }
                }
                '$' -> {
                    when (state) {
                        State.SINGLE_QUOTING -> currentStringPart += ch
                        else -> {
                            interpolateVariableName()
                            variableName = ""
                        }
                    }
                }
                '|' -> {
                    when (state) {
                        State.DOUBLE_QUOTING -> {
                            interpolateVariableName()
                            currentStringPart += ch
                        }
                        State.SINGLE_QUOTING -> currentStringPart += ch
                        State.NO_QUOTING -> {
                            interpolateVariableName()
                            if (currentStringPart != "") {
                                result.add(currentStringPart)
                                currentStringPart = ""
                            }
                            result.add("|")
                        }
                    }
                }
                ' ' ->
                    when (state) {
                        State.DOUBLE_QUOTING -> {
                            interpolateVariableName()
                            currentStringPart += ch
                        }
                        State.SINGLE_QUOTING -> currentStringPart += ch
                        State.NO_QUOTING -> {
                            interpolateVariableName()
                            if (currentStringPart != "") {
                                result.add(currentStringPart)
                                currentStringPart = ""
                            }
                        }
                    }
                '=' ->
                    when (state) {
                        State.DOUBLE_QUOTING -> {
                            interpolateVariableName()
                            currentStringPart += ch
                        }
                        State.SINGLE_QUOTING -> currentStringPart += ch
                        State.NO_QUOTING -> {
                            interpolateVariableName()
                            if (currentStringPart == "") {
                                currentStringPart += ch
                            } else {
                                result.add(currentStringPart)
                                currentStringPart = ""
                                result.add("=")
                                result[result.lastIndex] = result[result.lastIndex - 1].also {
                                    result[result.lastIndex - 1] = result[result.lastIndex]
                                }
                            }
                        }
                    }
                else ->
                    addChar(ch)
            }
        }

        if (state != State.NO_QUOTING) {
            throw IncorrectQuotingException("Incorrect quotes!")
        }

        interpolateVariableName()
        return if (currentStringPart == "") result else result.apply { add(currentStringPart) }
    }

    private fun interpolateVariableName() {
        currentStringPart += variableName?.let { GlobalEnvironment.getValue(it) }.orEmpty()
        variableName = null
    }

    private fun addChar(ch: Char) {
        if (variableName == null) currentStringPart += ch else variableName += ch
    }

    private fun clear() {
        state = State.NO_QUOTING
        variableName = null
        currentStringPart = ""
    }
}
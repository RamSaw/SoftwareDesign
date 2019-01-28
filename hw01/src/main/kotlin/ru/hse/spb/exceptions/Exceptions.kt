package ru.hse.spb.exceptions

sealed class CliException(message: String?) : Exception(message)

class UnknownCommandException(message: String?) : CliException(message)

class WrongCommandArguments(message: String?) : CliException(message)
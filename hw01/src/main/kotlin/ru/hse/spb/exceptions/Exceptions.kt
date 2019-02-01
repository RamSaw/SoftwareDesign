package ru.hse.spb.exceptions

/**
 * Base class for all exception in the app
 */
sealed class CliException(message: String?) : Exception(message)

/**
 * Exception that must be thrown if command is unknown
 */
class UnknownCommandException(message: String?) : CliException(message)

/**
 * Exception that must be thrown if external command has errored
 */
class ExternalCommandException(message: String?) : CliException(message)

/**
 * Exception that must be thrown if command has incorrect arguments
 */
class WrongCommandArgumentsException(message: String?) : CliException(message)

/**
 * Exception that must be thrown if quoting in input is incorrect
 */
class IncorrectQuotingException(message: String?) : CliException(message)
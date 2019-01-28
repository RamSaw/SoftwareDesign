package ru.hse.spb.environment

/**
 * This object is used to store global variables.
 * Only global variables are supported.
 */
object GlobalEnvironment {
    private var variables = HashMap<String, String>()

    /**
     * Sets new value for the variable.
     */
    fun setVariable(name: String, value: String) {
        variables[name] = value
    }

    /**
     * Returns current value for the variable.
     * If there is no value then it returns null.
     */
    fun getValue(name: String): String? = variables[name]
}
package ru.hse.spb.actions

import ru.hse.spb.model.Model
import java.io.*

/**
 * Represents user action.
 */
interface Action : Serializable {
    /**
     * Applies action to the model.
     */
    fun execute(model: Model)

    companion object {
        fun toByteArray(action: Action): ByteArray {
            val stream = ByteArrayOutputStream()
            ObjectOutputStream(stream).use {
                it.writeObject(action)
            }
            return stream.toByteArray()
        }

        fun fromByteArray(byteArray: ByteArray): Action {
            return ObjectInputStream(ByteArrayInputStream(byteArray)).readObject() as Action
        }
    }
}

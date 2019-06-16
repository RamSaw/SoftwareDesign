package ru.hse.spb.model.engine

import java.io.Serializable

/**
 * This class represents equipment which gives extra stats to player.
 */
data class Equipment(
    val name: String,
    private var isOnCharacter: Boolean,
    val additionalHealth: Int,
    val additionalStrength: Int
) : Serializable{
    fun isOnCharacter() = isOnCharacter

    fun takeOff() {
        isOnCharacter = false
    }

    fun takeOn() {
        isOnCharacter = true
    }
}
package ru.hse.spb.model.engine

data class Equipment(
    val name: String,
    var isOnCharacter: Boolean,
    val additionalHealth: Int,
    val additionalStrength: Int
) {
    fun takeOff() {
        isOnCharacter = false
    }

    fun takeOn() {
        isOnCharacter = true
    }
}
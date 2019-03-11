package ru.hse.spb.model.enigne

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
package ru.hse.spb.model.engine

import ru.hse.spb.model.Map.MapPosition

abstract class GameCharacter {
    protected abstract var health: Int
    protected abstract var level: Int
    protected abstract var strength: Int
    protected abstract var position: MapPosition

    fun getCurrentPosition() = position

    fun takeDamage(dmg: Int) {
        health -= dmg
    }

    fun getCurrentHealth() = health

    fun inclineDamage() = strength

    fun changePosition(x: Int, y: Int) {
        position.x = x
        position.y = y
    }
}
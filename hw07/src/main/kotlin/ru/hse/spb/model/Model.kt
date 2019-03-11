package ru.hse.spb.model

import ru.hse.spb.controller.Controller.Companion.PlayerAction

interface Model {
    fun move(action: PlayerAction)
}

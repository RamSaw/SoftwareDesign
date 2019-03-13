package ru.hse.spb.view

import ru.hse.spb.controller.Controller
import ru.hse.spb.model.Model

interface View {
    fun draw(model: Model)
    fun getAction(): Controller.Companion.PlayerAction
    fun stop()
}

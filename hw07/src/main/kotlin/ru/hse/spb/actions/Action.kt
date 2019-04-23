package ru.hse.spb.actions

import ru.hse.spb.model.Model

interface Action {
    fun execute(model: Model)
}

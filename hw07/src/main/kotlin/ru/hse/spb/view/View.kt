package ru.hse.spb.view

import ru.hse.spb.model.Model

interface View {
    fun draw(model: Model)
}

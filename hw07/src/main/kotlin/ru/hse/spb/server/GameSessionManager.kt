package ru.hse.spb.server

import ru.hse.spb.model.Model

interface GameSessionManager {
    fun getOrCreate(sessionName: String): Model

    fun listSessions(): Set<String>

    fun get(sessionName: String): Model
}
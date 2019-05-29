package ru.hse.spb.server

import ru.hse.spb.model.Map
import ru.hse.spb.model.Model
import ru.hse.spb.model.WorldModel

class GameSessionManagerImpl : GameSessionManager {
    override fun get(sessionName: String): Model = sessions.getValue(sessionName)

    override fun listSessions() = sessions.keys

    private val sessions = HashMap<String, Model>()

    override fun getOrCreate(sessionName: String): Model = sessions.getOrPut(sessionName, defaultValue = { WorldModel(Map.generate()) })
}
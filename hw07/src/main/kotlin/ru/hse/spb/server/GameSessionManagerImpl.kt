package ru.hse.spb.server

import io.grpc.stub.StreamObserver
import ru.hse.spb.model.Map
import ru.hse.spb.model.Model
import ru.hse.spb.model.WorldModel
import ru.hse.spb.roguelike.ServerReply

class GameSessionManagerImpl : GameSessionManager {
    override fun get(sessionName: String): Model = models.getValue(sessionName)

    override fun listSessions() = models.keys

    private val models = HashMap<String, Model>()
    private val clients = HashMap<String, MutableSet<StreamObserver<ServerReply>>>()

    override fun addClient(sessionName: String, client: StreamObserver<ServerReply>) =
        clients.getOrPut(sessionName, { mutableSetOf() }).add(client)

    override fun getClients(sessionName: String): Set<StreamObserver<ServerReply>> =
        clients.getValue(sessionName)

    override fun getOrCreate(sessionName: String): Model =
        models.getOrPut(sessionName, defaultValue = { WorldModel(Map.generate()) })

    override fun removeClient(sessionName: String, client: StreamObserver<ServerReply>) =
            clients[sessionName]!!.remove(client)
}
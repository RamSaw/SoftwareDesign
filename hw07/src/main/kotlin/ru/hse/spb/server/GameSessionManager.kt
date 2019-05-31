package ru.hse.spb.server

import io.grpc.stub.StreamObserver
import ru.hse.spb.model.Model
import ru.hse.spb.roguelike.ServerReply

interface GameSessionManager {
    fun getOrCreate(sessionName: String): Model

    fun listSessions(): Set<String>

    fun get(sessionName: String): Model
    fun addClient(sessionName: String, client: StreamObserver<ServerReply>): Boolean
    fun getClients(sessionName: String): Set<StreamObserver<ServerReply>>
    fun removeClient(sessionName: String, client: StreamObserver<ServerReply>): Boolean
}
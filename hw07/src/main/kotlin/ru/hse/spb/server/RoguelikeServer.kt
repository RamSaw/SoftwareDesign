package ru.hse.spb.server

import com.google.protobuf.ByteString
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import ru.hse.spb.actions.Action
import ru.hse.spb.model.FailedLoadException
import ru.hse.spb.roguelike.ConnectionSetUpperGrpc
import ru.hse.spb.roguelike.PlayerRequest
import ru.hse.spb.roguelike.ServerReply
import java.io.IOException
import java.util.logging.Level
import java.util.logging.Logger

/**
 * Roguelike server.
 */
class RoguelikeServer(private val port: Int) {
    private var server: Server? = null
    private val gameSessions = GameSessionManagerImpl()

    @Throws(IOException::class)
    private fun start() {
        server = ServerBuilder.forPort(port)
            .addService(ConnectionSetUpperImpl())
            .build()
            .start()
        logger.log(Level.INFO, "Server started, listening on {0}", port)
        Runtime.getRuntime().addShutdownHook(object : Thread() {
            override fun run() {
                // Use stderr here since the logger may have been reset by its JVM shutdown hook.
                System.err.println("*** shutting down gRPC server since JVM is shutting down")
                this@RoguelikeServer.stop()
                System.err.println("*** server shut down")
            }
        })
    }

    private fun stop() {
        server?.shutdown()
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    @Throws(InterruptedException::class)
    private fun blockUntilShutdown() {
        server?.awaitTermination()
    }

    private inner class ConnectionSetUpperImpl : ConnectionSetUpperGrpc.ConnectionSetUpperImplBase() {
        private fun sendModelToAllPlayers(sessionName: String, responseBuilder: ServerReply.Builder = ServerReply.newBuilder()) {
            val model = gameSessions.getOrCreate(sessionName)
            responseBuilder.model = ByteString.copyFrom(model.toByteArray())
            val response = responseBuilder.build()
            for (client in gameSessions.getClients(sessionName)) {
                client.onNext(response)
            }
        }

        private fun sendErrosMessage(errorMessage: String, client: StreamObserver<ServerReply>) {
            val response = ServerReply.newBuilder().setErrorMessage(errorMessage).build()
            client.onNext(response)
        }

        override fun communicate(responseObserver: StreamObserver<ServerReply>?): StreamObserver<PlayerRequest>? {
            return object: StreamObserver<PlayerRequest> {
                private var playerId: Int? = null
                private var sessionName: String? = null

                override fun onNext(value: PlayerRequest?) {
                    if (value!!.sessionName == "list") {
                        val sessionsToPrint = gameSessions.listSessions().joinToString("\n")
                        val reply = ServerReply.newBuilder().setSessions(sessionsToPrint).build()
                        responseObserver!!.onNext(reply)
                        return
                    }
                    else if (sessionName == null) {
                        val responseBuilder = ServerReply.newBuilder()
                        sessionName = value.sessionName
                        gameSessions.addClient(sessionName!!, responseObserver!!)
                        val model = gameSessions.getOrCreate(sessionName!!)
                        playerId = model.addPlayer()
                        responseBuilder.playerId = playerId.toString()
                        sendModelToAllPlayers(sessionName!!, responseBuilder)
                    }
                    else if (!value.action.isEmpty) {
                        val model = gameSessions.getOrCreate(sessionName!!)
                        if (playerId != model.getActivePlayer()) {
                            return
                        }
                        var errorMessage: String? = null
                        try {
                            Action.fromByteArray(value.action.toByteArray()).execute(model)
                        } catch (e: FailedLoadException) {
                            errorMessage = "Failed loading map of saved game. Probably you have no saved games.\n" +
                                    "Exception message: " + e.message
                        } catch (e: Exception) {
                            errorMessage = "Unexpected exception: " + e.message
                        }
                        if (errorMessage != null) {
                            sendErrosMessage(errorMessage, responseObserver!!)
                        } else {
                            sendModelToAllPlayers(sessionName!!)
                        }
                    }
                }

                override fun onError(t: Throwable?) {
                    responseObserver!!.onError(t)
                }

                override fun onCompleted() {
                    gameSessions.get(sessionName!!).removePlayer(playerId!!)
                    gameSessions.removeClient(sessionName!!, responseObserver!!)
                    sendModelToAllPlayers(sessionName!!)
                }
            }
        }
    }

    companion object {
        private val logger = Logger.getLogger(RoguelikeServer::class.java.name)

        /**
         * Main launches the server from the command line.
         */
        @Throws(IOException::class, InterruptedException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 1) {
                printUsage()
                return
            }
            val port = args[0].toInt()
            val server = RoguelikeServer(port)
            server.start()
            server.blockUntilShutdown()
        }

        private fun printUsage() {
            println("Args: <port>")
        }
    }
}
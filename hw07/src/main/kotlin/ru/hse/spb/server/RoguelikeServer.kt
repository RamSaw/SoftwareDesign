package ru.hse.spb.server

import com.google.protobuf.ByteString
import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import ru.hse.spb.actions.Action
import ru.hse.spb.model.Model
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
                    val responseBuilder = ServerReply.newBuilder()
                    var model: Model?
                    if (sessionName == null) {
                        sessionName = value.sessionName
                        model = gameSessions.getOrCreate(sessionName!!)
                        playerId = model.addPlayer()
                        responseBuilder.playerId = playerId.toString()
                    }
                    model = gameSessions.getOrCreate(sessionName!!)
                    if (!value.action.isEmpty) {
                        Action.fromByteArray(value.action.toByteArray()).execute(model)
                    }
                    responseBuilder.model = ByteString.copyFrom(model.toByteArray())
                    responseObserver!!.onNext(responseBuilder.build())
                }

                override fun onError(t: Throwable?) {
                    responseObserver!!.onError(t)
                }

                override fun onCompleted() {
                    gameSessions.get(sessionName!!).removePlayer(playerId!!)
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
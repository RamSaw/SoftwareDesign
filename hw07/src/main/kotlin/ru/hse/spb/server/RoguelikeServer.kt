package ru.hse.spb.server

import io.grpc.Server
import io.grpc.ServerBuilder
import io.grpc.stub.StreamObserver
import ru.hse.spb.roguelike.ConnectionReply
import ru.hse.spb.roguelike.ConnectionRequest
import ru.hse.spb.roguelike.ConnectionSetUpperGrpc
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
        override fun tryToConnect(req: ConnectionRequest, responseObserver: StreamObserver<ConnectionReply>) {
            if (req.sessionName == "list") {
                val sessionsToPrint = gameSessions.listSessions().joinToString("\n")
                val reply = ConnectionReply.newBuilder().setPlayerId(sessionsToPrint).build()
                responseObserver.onNext(reply)
                responseObserver.onCompleted()
                return
            }
            val model = gameSessions.getOrCreate(req.sessionName)
            val playerId = model.addPlayer()
            val reply = ConnectionReply.newBuilder().setPlayerId(playerId.toString()).build()
            responseObserver.onNext(reply)
            responseObserver.onCompleted()
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
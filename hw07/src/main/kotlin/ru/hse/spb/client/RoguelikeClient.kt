package ru.hse.spb.client

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
import ru.hse.spb.RoguelikeSinglePlayerApplication
import ru.hse.spb.controller.Controller
import ru.hse.spb.model.Map
import ru.hse.spb.model.Model
import ru.hse.spb.model.WorldModel
import ru.hse.spb.roguelike.ConnectionSetUpperGrpc
import ru.hse.spb.roguelike.PlayerRequest
import ru.hse.spb.roguelike.ServerReply
import ru.hse.spb.view.ConsoleView
import ru.hse.spb.view.View
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference
import java.util.logging.Logger


/**
 * Roguelike client application.
 */
class RoguelikeClient
internal constructor(private val channel: ManagedChannel) {
    private val stub: ConnectionSetUpperGrpc.ConnectionSetUpperStub
            = ConnectionSetUpperGrpc.newStub(channel)

    constructor(host: String, port: Int) : this(ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build()) {
        communicatorRef.set(communicator)
    }


    @Throws(InterruptedException::class)
    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    private var view : View? = null
    private val model: Model = WorldModel(Map.generate())
    private var isGameInitialized = false
    private val lockToWait = Object()
    private var isFinished = false
    private var isListLastOperation = false

    val communicatorRef = AtomicReference<StreamObserver<PlayerRequest>>()
    private val communicator = stub.communicate(object: StreamObserver<ServerReply> {
        override fun onNext(value: ServerReply?) {
            if (isListLastOperation) {
                println("Sessions are:")
                println(value!!.sessions)
                return
            }
            if (view == null) {
                println("Starting game")
                model.updateFromByteArray(value!!.model.toByteArray())
                view = ConsoleView(value.playerId.toInt())
                isGameInitialized = true
            } else {
                model.updateFromByteArray(value!!.model.toByteArray())
            }
            view!!.draw(model)
            val threadToReadInput = Thread(Runnable { Controller.makeOnlineTurn(model, view!!, communicatorRef.get()) })
            threadToReadInput.isDaemon = true
            threadToReadInput.start()
        }

        override fun onError(t: Throwable?) {
        }

        override fun onCompleted() {
            println("Ending game")
            synchronized(lockToWait) {
                isFinished = true
                lockToWait.notify()
            }
        }
    })

    /** Connect to server.  */
    fun connect(name: String) {
        println("Will try to connect to {$name} session...")
        val request = PlayerRequest.newBuilder().setSessionName(name).build()
        isListLastOperation = false
        communicator.onNext(request)
    }

    /** List current sessions on server */
    fun list() {
        val request = PlayerRequest.newBuilder().setSessionName("list").build()
        isListLastOperation = true
        communicator.onNext(request)
    }

    companion object {
        private val logger = Logger.getLogger(RoguelikeClient::class.java.name)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size > 2) {
                printUsage()
                return
            }
            if (args.size < 2) {
                RoguelikeSinglePlayerApplication.start()
                return
            }
            val client = RoguelikeClient(args[0], args[1].toInt())
            try {
                /* Access a service running on the local machine on port 50051 */
                var sessionIsChosen = false
                while (!sessionIsChosen) {
                    println("Please enter 'list' command to list sessions or enter session name")
                    val input = readLine()
                    if (input == "list") {
                        client.list()
                    } else {
                        client.connect(input!!)
                        sessionIsChosen = true
                    }
                }
                while (!client.isFinished) {
                    synchronized(client.lockToWait) {
                        client.lockToWait.wait()
                    }
                }
            } finally {
                client.shutdown()
            }
        }

        private fun printUsage() {
            println("Args: <ip> <port> or empty. Last case is single player mode")
        }
    }
}
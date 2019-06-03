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
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference


/**
 * Roguelike client application.
 */
class RoguelikeClient
internal constructor(private val channel: ManagedChannel) {
    private val stub: ConnectionSetUpperGrpc.ConnectionSetUpperStub
            = ConnectionSetUpperGrpc.newStub(channel)
    private var view : View? = null
    private val model: Model = WorldModel(Map.generate())
    private val lockToWait = Object()
    private var isFinished = false
    private var isListLastOperation = false
    private val isGameInitialized = AtomicBoolean(false)
    private val communicatorRef = AtomicReference<StreamObserver<PlayerRequest>>()

    private inner class ServerResponseHandler : StreamObserver<ServerReply> {
        override fun onNext(value: ServerReply?) {
            if (value!!.errorMessage.isNotEmpty()) {
                System.err.println("Error occurred on server after your action.")
                System.err.println("Server response:")
                System.err.println(value.errorMessage)
                return
            }
            if (isListLastOperation) {
                println("Sessions are:")
                println(value.sessions)
                return
            }
            if (view == null) {
                println("Starting game")
                model.updateFromByteArray(value.model.toByteArray())
                view = ConsoleView(value.playerId.toInt())
                isGameInitialized.set(true)
            } else {
                model.updateFromByteArray(value.model.toByteArray())
            }
            view!!.draw(model)
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
    }

    private val communicator = stub.communicate(ServerResponseHandler())

    constructor(host: String, port: Int) : this(ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build()) {
        communicatorRef.set(communicator)
    }

    @Throws(InterruptedException::class)
    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

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

    fun start() {
        var sessionIsChosen = false
        while (!sessionIsChosen) {
            println("Please enter 'list' command to list sessions or enter session name")
            val input = readLine()
            if (input == "list") {
                list()
            } else {
                connect(input!!)
                val threadToReadInput = Thread(Runnable {
                    while (!isGameInitialized.get()) {
                        continue
                    }
                    while (!isFinished) {
                        Controller.makeOnlineTurn(view!!, communicatorRef.get())
                    }
                })
                threadToReadInput.start()
                sessionIsChosen = true
            }
        }
        while (!isFinished) {
            synchronized(lockToWait) {
                lockToWait.wait()
            }
        }
    }
}
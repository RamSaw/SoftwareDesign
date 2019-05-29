/*
 * Copyright 2015 The gRPC Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ru.hse.spb.client

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.StreamObserver
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


    val communicatorRef = AtomicReference<StreamObserver<PlayerRequest>>()
    private val communicator = stub.communicate(object: StreamObserver<ServerReply> {
        override fun onNext(value: ServerReply?) {
            if (value!!.sessions.isNotBlank()) {
                println("Sessions are:")
                println(value.sessions)
                return
            }
            if (view == null) {
                println("Starting game")
                view = ConsoleView(value.playerId.toInt())
                model.updateFromByteArray(value.model.toByteArray())
                isGameInitialized = true
            } else {
                model.updateFromByteArray(value.model.toByteArray())
            }
            view!!.draw(model)
            Controller.makeTurn(model, view!!, communicatorRef.get())
        }

        override fun onError(t: Throwable?) {
        }

        override fun onCompleted() {
            println("Ending game")
        }
    })

    /** Connect to server.  */
    fun connect(name: String) {
        println("Will try to connect to {$name} session...")
        val request = PlayerRequest.newBuilder().setSessionName(name).build()
        communicator.onNext(request)
    }

    /** List current sessions on server */
    fun list() {
        val request = PlayerRequest.newBuilder().setSessionName("list").build()
        communicator.onNext(request)
    }

    companion object {
        private val logger = Logger.getLogger(RoguelikeClient::class.java.name)

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size != 2) {
                printUsage()
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
            } finally {
                client.shutdown()
            }
        }

        private fun printUsage() {
            println("Args: <ip> <port>")
        }
    }
}
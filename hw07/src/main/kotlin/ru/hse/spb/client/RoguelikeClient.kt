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
import io.grpc.StatusRuntimeException
import ru.hse.spb.roguelike.ConnectionReply
import ru.hse.spb.roguelike.ConnectionRequest
import ru.hse.spb.roguelike.ConnectionSetUpperGrpc
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

/**
 * Roguelike client application.
 */
class RoguelikeClient
internal constructor(private val channel: ManagedChannel) {
    private val blockingStub: ConnectionSetUpperGrpc.ConnectionSetUpperBlockingStub
            = ConnectionSetUpperGrpc.newBlockingStub(channel)

    constructor(host: String, port: Int) : this(ManagedChannelBuilder.forAddress(host, port)
        .usePlaintext()
        .build())


    @Throws(InterruptedException::class)
    fun shutdown() {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS)
    }

    /** Connect to server.  */
    fun connect(name: String) {
        println("Will try to connect to {$name} session...")
        val request = ConnectionRequest.newBuilder().setSessionName(name).build()
        val response: ConnectionReply =  try {
            blockingStub.tryToConnect(request)
        } catch (e: StatusRuntimeException) {
            System.err.println("RPC failed: {${e.status}}")
            return
        }

        println("Your player id: ${response.playerId}")
    }

    /** List current sessions on server */
    fun list() {
        val request = ConnectionRequest.newBuilder().setSessionName("list").build()
        val response: ConnectionReply =  try {
            blockingStub.tryToConnect(request)
        } catch (e: StatusRuntimeException) {
            System.err.println("RPC failed: {${e.status}}")
            return
        }

        println("Sessions:")
        println(response.playerId)
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
                println("Starting game")
            } finally {
                client.shutdown()
            }
        }

        private fun printUsage() {
            println("Args: <ip> <port>")
        }
    }
}
package ru.hse.spb.client

import ru.hse.spb.RoguelikeSinglePlayerApplication

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
        client.start()
    } finally {
        client.shutdown()
    }
}

fun printUsage() {
    println("Args: <ip> <port> or empty. Last case is single player mode")
}

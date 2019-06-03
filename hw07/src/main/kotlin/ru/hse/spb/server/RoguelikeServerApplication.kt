package ru.hse.spb.server

/**
 * Main launches the server from the command line.
 */
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

fun printUsage() {
    println("Args: <port>. Preferable is 50051 port.")
}

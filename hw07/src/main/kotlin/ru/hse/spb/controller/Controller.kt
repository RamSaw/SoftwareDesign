package ru.hse.spb.controller

import io.grpc.stub.StreamObserver
import ru.hse.spb.model.Model
import ru.hse.spb.roguelike.PlayerRequest
import ru.hse.spb.view.View

/**
 * This class interacts with user and provides actions to model.
 */
class Controller(private val model: Model,
                 private val view: View,
                 private val communicator: StreamObserver<PlayerRequest>) {
    companion object {
        fun makeTurn(model: Model,
                     view: View,
                     communicator: StreamObserver<PlayerRequest>) {
            if (model.getActivePlayer() != view.getPlayerId()) {
                return
            }
            val action = view.getAction()
            action.execute(model)
            view.draw(model)
        }
    }


    /**
     * Main game loop.
     */
    fun run() {
        while (true) {
            val action = view.getAction()
            action.execute(model)

            if (model.isGameFinished()) {
                break
            }
        }
    }
}
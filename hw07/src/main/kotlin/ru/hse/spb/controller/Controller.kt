package ru.hse.spb.controller

import com.google.protobuf.ByteString
import io.grpc.stub.StreamObserver
import ru.hse.spb.actions.Action
import ru.hse.spb.model.Model
import ru.hse.spb.roguelike.PlayerRequest
import ru.hse.spb.view.View

/**
 * This class interacts with user and provides actions to model.
 */
class Controller(private val model: Model,
                 private val view: View) {
    companion object {
        fun makeTurn(model: Model,
                     view: View,
                     communicator: StreamObserver<PlayerRequest>) {
            if (model.getActivePlayer() != view.getPlayerId()) {
                return
            }
            val action = view.getAction()
            communicator.onNext(PlayerRequest.newBuilder()
                .setAction(ByteString.copyFrom(Action.toByteArray(action))).build())
        }
    }


    /**
     * Main game loop.
     */
    fun run() {
        while (true) {
            view.draw(model)
            val action = view.getAction()
            action.execute(model)

            if (model.isGameFinished()) {
                break
            }
        }
    }
}
package ru.hse.spb.controller

import com.google.protobuf.ByteString
import io.grpc.stub.StreamObserver
import ru.hse.spb.actions.Action
import ru.hse.spb.actions.QuitGameAction
import ru.hse.spb.model.Model
import ru.hse.spb.roguelike.PlayerRequest
import ru.hse.spb.view.View
import kotlin.system.exitProcess

/**
 * This class interacts with user and provides actions to model.
 */
class Controller(private val model: Model,
                 private val view: View) {
    companion object {
        fun makeOnlineTurn(model: Model,
                           view: View,
                           communicator: StreamObserver<PlayerRequest>) {
            if (model.getActivePlayer() != view.getPlayerId()) {
                return
            }
            val action = view.getAction(model)
            if (action is QuitGameAction) {
                communicator.onCompleted()
                exitProcess(0)
            }
            if (action != null) {
                communicator.onNext(PlayerRequest.newBuilder()
                    .setAction(ByteString.copyFrom(Action.toByteArray(action))).build())
            }
        }
    }


    /**
     * Main game loop.
     */
    fun run() {
        while (true) {
            view.draw(model)
            val action = view.getAction(model)
            action!!.execute(model)

            if (model.isGameFinished()) {
                break
            }
        }
    }
}
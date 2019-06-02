package ru.hse.spb.controller

import com.google.protobuf.ByteString
import io.grpc.stub.StreamObserver
import ru.hse.spb.actions.Action
import ru.hse.spb.actions.QuitGameAction
import ru.hse.spb.model.FailedLoadException
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
        fun makeOnlineTurn(
            view: View,
            communicator: StreamObserver<PlayerRequest>
        ) {
            val action = view.getAction()
            if (action is QuitGameAction) {
                communicator.onCompleted()
                exitProcess(0)
            }
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
            try {
                action.execute(model)
            } catch (e: FailedLoadException) {
                System.err.println("Failed loading map of saved game. Probably you have no saved games.")
                System.err.println("Exception message: " + e.message)
            } catch (e: Exception) {
                System.err.println("Unexpected exception: " + e.message)
            }

            if (model.isGameFinished()) {
                break
            }
        }
    }
}
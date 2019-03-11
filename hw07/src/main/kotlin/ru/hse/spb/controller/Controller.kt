package ru.hse.spb.controller

class Controller {

    public fun processTurn(action:PlayerAction){
       TODO()
    }

    companion object {
        enum class PlayerAction {
            MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT, TAKE_OFF_EQUIPMENT, TAKE_ON_EQUIPMENT
        }
    }
}
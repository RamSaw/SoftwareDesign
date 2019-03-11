package ru.hse.spb.model

import java.io.File
import kotlin.random.Random

class Map private constructor(private val field: Array<Array<CellState>>) {
    companion object Factory {
        private const val FIELD_WIDTH = 10
        private const val FIELD_HEIGHT = 10
        private const val WALL_PERCENTAGE = 25

        private val random = Random(0)

        fun generate(): Map {
            val field = Array(FIELD_HEIGHT) { Array(FIELD_WIDTH) { CellState.FREE } }

            for (row in field) {
                for (i in row.indices) {
                    row[i] = when (random.nextInt(0, 100)) {
                        in 0..WALL_PERCENTAGE -> CellState.WALL
                        else -> CellState.FREE
                    }
                }
            }

            return Map(field)
        }

        fun load(path: String): Map {
            val (height, width) = File(path).inputStream().bufferedReader().readLine()!!.split(' ').map(String::toInt)
            val field = Array(height) { Array(width) { CellState.FREE } }

            for (row in field) {
                for (i in row.indices) {
                    row[i] = when (random.nextInt(0, 100)) {
                        in 0..WALL_PERCENTAGE -> CellState.WALL
                        else -> CellState.FREE
                    }
                }
            }

            return Map(field)
        }
    }

    fun getStartCell(): MapPosition {
        TODO()
    }

    fun getFreeCells(): List<MapPosition> {
        TODO()
    }

    fun getCell(position: MapPosition): CellState {
        return field[position.x][position.y]
    }

    class MapPosition(var x: Int, var y: Int)

    enum class CellState {
        FREE,
        WALL
    }
}

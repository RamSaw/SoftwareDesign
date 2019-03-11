package ru.hse.spb.model

import java.io.File
import kotlin.random.Random

class Map private constructor(val field: Array<Array<CellState>>) {
    companion object Factory {
        private const val DEFAULT_FIELD_WIDTH = 10
        private const val DEFAULT_FIELD_HEIGHT = 10
        private const val WALL_PERCENTAGE = 25

        private val random = Random(0)

        fun generate(): Map {
            val field = Array(DEFAULT_FIELD_HEIGHT) { Array(DEFAULT_FIELD_WIDTH) { CellState.FREE } }

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
            val reader = File(path).inputStream().bufferedReader()
            val (height, width) = reader.readLine().split(' ').map(String::toInt)
            val field = Array(height) { Array(width) { CellState.FREE } }

            for (row in field) {
                val currentLine = reader.readLine()

                for (i in row.indices) {
                    row[i] = when (currentLine[i]) {
                        '.' -> CellState.FREE
                        else -> CellState.WALL
                    }
                }
            }

            return Map(field)
        }
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

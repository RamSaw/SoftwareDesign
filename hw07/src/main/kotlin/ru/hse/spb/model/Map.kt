package ru.hse.spb.model

import java.io.File
import java.io.Serializable
import kotlin.random.Random

/**
 * This class represents game field.
 */
class Map private constructor(val field: Array<Array<CellState>>) : Serializable {
    companion object Factory {
        private const val DEFAULT_FIELD_WIDTH = 20
        private const val DEFAULT_FIELD_HEIGHT = 20
        private const val WALL_PERCENTAGE = 25

        /**
         * Generates new map with random walls.
         */
        fun generate(): Map {
            val field = Array(DEFAULT_FIELD_HEIGHT) { Array(DEFAULT_FIELD_WIDTH) { CellState.FREE } }

            for ((i, row) in field.withIndex()) {
                for (j in row.indices) {
                    if (i == 0 || i == DEFAULT_FIELD_HEIGHT - 1 ||
                        j == 0 || j == DEFAULT_FIELD_WIDTH - 1) {
                        row[j] = CellState.WALL
                    } else {
                        row[j] = when ((0..100).random()) {
                            in 0..WALL_PERCENTAGE -> CellState.WALL
                            else -> CellState.FREE
                        }
                    }
                }
            }

            return Map(field)
        }

        /**
         * Loads map from a file.
         */
        fun load(path: String): Map {
            val reader = File(path).inputStream().bufferedReader()
            val (height, width) = reader.readLine().split(' ').map(String::toInt)
            val field = Array(height) { Array(width) { CellState.FREE } }

            for (row in field) {
                val currentLine = reader.readLine()

                for (i in row.indices) {
                    row[i] = when (currentLine[i]) {
                        ' ' -> CellState.FREE
                        else -> CellState.WALL
                    }
                }
            }

            return Map(field)
        }
    }

    /**
     * Map width getter.
     */
    fun getWidth(): Int {
        return when {
            field.isNotEmpty() -> field[0].size
            else -> 0
        }
    }

    /**
     * Selects players start cell.
     */
    fun getStartCell(): MapPosition {
        val freeCells = getFreeCells()
        val i = Random(0).nextInt(freeCells.size)

        return freeCells[i]
    }

    /**
     * Returns all fee cell positions.
     */
    fun getFreeCells(): List<MapPosition> {
        val free = mutableListOf<MapPosition>()

        for ((i, row) in field.withIndex()) {
            for (j in row.indices) {
                if (row[j] == CellState.FREE) {
                    free.add(MapPosition(j, i))
                }
            }
        }

        return free
    }

    /**
     * Returns cell state for a given position.
     */
    fun getCell(position: MapPosition): CellState {
        return when {
            position.y !in 0 until field.size -> CellState.WALL
            field.isNotEmpty() && position.x !in 0 until field[0].size -> CellState.WALL
            else -> field[position.y][position.x]
        }
    }

    fun changeCellState(pos:MapPosition, state:CellState){
        field[pos.y][pos.x] = state
    }

    /**
     * This class represents map coordinates.
     */
    data class MapPosition(var x: Int, var y: Int) : Serializable

    /**
     * This class represents map cell state.
     */
    enum class CellState {
        FREE,
        OCCUPIED,
        WALL
    }
}

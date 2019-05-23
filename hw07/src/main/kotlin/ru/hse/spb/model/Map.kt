package ru.hse.spb.model

import java.io.File
import kotlin.random.Random

/**
 * This class represents game field.
 */
class Map private constructor(val field: Array<Array<CellState>>) {
    companion object Factory {
        private const val DEFAULT_FIELD_WIDTH = 20
        private const val DEFAULT_FIELD_HEIGHT = 20
        private const val WALL_PERCENTAGE = 25

        /**
         * Generates new map with random walls.
         */
        fun generate(): Map {
            var map = Map(generateField())

            while (!checkConnectivity(map)) {
                map = Map(generateField())
            }
            return map
        }

        private fun checkConnectivity(map: Map): Boolean {
            val visited = Array(DEFAULT_FIELD_HEIGHT) { Array(DEFAULT_FIELD_WIDTH) { false } }
            fun dfs(position: MapPosition) {
                if (visited[position.y][position.x])
                    return

                visited[position.y][position.x] = true
                for (j in -1..1)
                    for (i in -1..1) {
                        val newPosition = MapPosition(position.x + i, position.y + j)
                        if (map.getCell(newPosition) == CellState.FREE)
                            dfs(newPosition)
                    }
            }
            dfs(map.getStartCell())
            for (j in 0..(DEFAULT_FIELD_HEIGHT - 1))
                for (i in 0..(DEFAULT_FIELD_WIDTH - 1)) {
                    val positionToCheck = MapPosition(i, j)
                    if (!visited[positionToCheck.y][positionToCheck.x] &&
                        map.getCell(positionToCheck) == CellState.FREE) {
                        return false
                    }
                }
            return true
        }

        private fun generateField(): Array<Array<CellState>> {
            val field = Array(DEFAULT_FIELD_HEIGHT) { Array(DEFAULT_FIELD_WIDTH) { CellState.FREE } }

            for ((i, row) in field.withIndex()) {
                for (j in row.indices) {
                    if (i == 0 || i == DEFAULT_FIELD_HEIGHT - 1 ||
                        j == 0 || j == DEFAULT_FIELD_WIDTH - 1
                    ) {
                        row[j] = CellState.WALL
                    } else {
                        row[j] = when (Random.nextInt(0, 100)) {
                            in 0..WALL_PERCENTAGE -> CellState.WALL
                            else -> CellState.FREE
                        }
                    }
                }
            }
            return field
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
     * Selects player start cell.
     */
    fun getStartCell(): MapPosition {
        val freeCells = getFreeCells()
        val i = Random.nextInt(freeCells.size)

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
            position.x !in 0 until field[0].size -> CellState.WALL
            else -> field[position.y][position.x]
        }
    }

    /**
     * This class represents map coordinates.
     */
    data class MapPosition(var x: Int, var y: Int)

    /**
     * This class represents map cell state.
     */
    enum class CellState {
        FREE,
        WALL
    }
}

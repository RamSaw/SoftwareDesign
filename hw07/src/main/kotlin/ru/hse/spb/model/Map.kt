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
        fun generate(width: Int = DEFAULT_FIELD_WIDTH, height: Int = DEFAULT_FIELD_HEIGHT): Map {
            var map = Map(generateField(width, height))

            while (!checkConnectivity(map)) {
                map = Map(generateField(width, height))
            }
            return map
        }

        private enum class Direction(val dx: Int, val dy: Int) {
            UP(0, -1),
            RIGHT(1, 0),
            DOWN(0, 1),
            LEFT(-1, 0),
        }

        private fun checkConnectivity(map: Map): Boolean {
            val width = map.getWidth()
            val height = map.getHeight()
            val visited = Array(height) { Array(width) { false } }

            fun dfs(position: MapPosition) {
                if (visited[position.y][position.x])
                    return

                visited[position.y][position.x] = true
                for (delta in Direction.values()) {
                    val newPosition = MapPosition(position.x + delta.dx, position.y + delta.dy)
                    if (map.getCell(newPosition) == CellState.FREE)
                        dfs(newPosition)
                }
            }

            dfs(map.getStartCell())
            for (j in 0 until height)
                for (i in 0 until width) {
                    val positionToCheck = MapPosition(i, j)
                    if (!visited[positionToCheck.y][positionToCheck.x] &&
                        map.getCell(positionToCheck) == CellState.FREE) {
                        return false
                    }
                }
            return true
        }

        private fun generateField(width: Int, height: Int): Array<Array<CellState>> {
            val field = Array(height) { Array(width) { CellState.FREE } }

            for ((i, row) in field.withIndex()) {
                for (j in row.indices) {
                    if (i == 0 || i == height - 1 ||
                        j == 0 || j == width - 1
                    ) {
                        row[j] = CellState.WALL
                    } else {
                        row[j] = when ((0..100).random()) {
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
            try {
                val reader = File(path).inputStream().bufferedReader()
                val firstLine = reader.readLine() ?: throw MapFormatException("Expected height and width in the beginning")
                val (height, width) = firstLine.split(' ').map(String::toInt)
                val field = Array(height) { Array(width) { CellState.FREE } }

                for (row in field) {
                    val currentLine = reader.readLine()

                    if (currentLine == null || currentLine.length != width) {
                        throw MapFormatException("Unexpected field size")
                    }

                    for (i in row.indices) {
                        row[i] = when (currentLine[i]) {
                            ' ' -> CellState.FREE
                            else -> CellState.WALL
                        }
                    }
                }

                return Map(field)
            } catch (e: NumberFormatException) {
                throw MapFormatException("Expected two integers for field height and width")
            }
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
     * Map height getter.
     */
    fun getHeight(): Int {
        return field.size
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
     * Changes cell state to a given one.
     */
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

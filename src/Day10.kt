import java.util.*
import kotlin.math.abs

fun main() {

    // Left the initial speedy implementation without cleaning it up.
    fun part1(input: List<String>): Int {
        var startI = 0
        var startJ = 0
        for ((i, row) in input.withIndex()) {
            for ((j, c) in row.withIndex()) {
                if (c == 'S') {
                    startI = i
                    startJ = j
                }
            }
        }

        val visited = mutableMapOf(Pair(startI, startJ) to 0)
        val queue: Queue<Pair<Int, Int>> = LinkedList()
        queue.add(Pair(startI, startJ))

        while (queue.isNotEmpty()) {
            val currentPos = queue.poll()
            val (currentI, currentJ) = currentPos
            val currentDistance = visited[currentPos]!!

            val correctPipes = mutableMapOf(
                Pair(-1, 0) to setOf('|', '7', 'F'),
                Pair(0, 1) to setOf('-', 'J', '7'),
                Pair(1, 0) to setOf('|', 'L', 'J'),
                Pair(0, -1) to setOf('-', 'L', 'F')
            )

            for (correctPipe in correctPipes) {
                val (nextOffset, pipes) = correctPipe
                val nextI = currentI + nextOffset.first
                val nextJ = currentJ + nextOffset.second

                if (nextI !in input.indices || nextJ !in input[nextI].indices) {
                    continue
                }

                val nextPos = Pair(nextI, nextJ)
                val nextPipe = input[nextI][nextJ]

                if (nextPipe in pipes && nextPos !in visited) {
                    visited[nextPos] = currentDistance + 1
                    queue.add(nextPos)
                }
            }
        }
        return visited.values.max()
    }


    data class Pos(val x: Int, val y: Int)

    operator fun Pos.plus(other: Pos): Pos {
        return Pos(x + other.x, y + other.y)
    }

    data class State(val position: Pos, val direction: Char)

    // Points need to be sorted (counter) clockwise.
    fun shoelaceArea(points: List<Pos>): Double {
        return abs((points.zipWithNext { current, next ->
            current.x * next.y - next.x * current.y
        }.sumOf { it } + points.last().x * points.first().y - points.first().x * points.last().y) / 2.0)
    }

    val pipeToDirection = mapOf(
        '|' to mapOf('N' to 'N', 'S' to 'S'),
        '-' to mapOf('E' to 'E', 'W' to 'W'),
        '7' to mapOf('E' to 'S', 'N' to 'W'),
        'F' to mapOf('N' to 'E', 'W' to 'S'),
        'J' to mapOf('E' to 'N', 'S' to 'W'),
        'L' to mapOf('W' to 'N', 'S' to 'E'),
        'S' to mapOf('N' to 'N', 'S' to 'S', 'E' to 'E', 'W' to 'W')
    )

    val directionToOffset = mapOf(
        'N' to Pos(-1, 0),
        'S' to Pos(1, 0),
        'E' to Pos(0, 1),
        'W' to Pos(0, -1),
    )

    // Cleaned up and rewritten.
    fun part2(input: List<String>): Int {
        val startPos = input
            .flatMapIndexed { i, row -> row.mapIndexedNotNull { j, c -> if (c == 'S') Pos(i, j) else null } }
            .firstOrNull() ?: throw IllegalArgumentException("No starting position????")

        val startDir = when {
            input[startPos.x + 1][startPos.y] in setOf('|', 'J', 'L') -> 'S'
            input[startPos.x - 1][startPos.y] in setOf('|', 'F', '7') -> 'N'
            input[startPos.x][startPos.y + 1] in setOf('-', 'J', '7') -> 'E'
            input[startPos.x][startPos.y - 1] in setOf('-', 'F', 'L') -> 'W'
            else -> throw IllegalArgumentException("No starting direction????")
        }

        val pipePositions = generateSequence(State(startPos, startDir)) { (currentPos, currentDir) ->
            val currentPipe = input[currentPos.x][currentPos.y]
            val newDir = pipeToDirection[currentPipe]?.get(currentDir) ?: error("Invalid pipe / direction")
            val newPos = currentPos + directionToOffset[newDir]!!
            if (newPos == startPos) null else State(newPos, newDir)
        }.map { it.position }.toList()

        // Pick's Theorem
        val A = shoelaceArea(pipePositions)
        val b = pipePositions.size

        return (A - b / 2 + 1).toInt()
    }

    fun part2InitialComplicatedIdea(input: List<String>): Int {
        val startPos = input
            .flatMapIndexed { i, row -> row.mapIndexedNotNull { j, c -> if (c == 'S') Pos(i, j) else null } }
            .firstOrNull() ?: throw IllegalArgumentException("No starting position????")


        val directions = setOf('N', 'S', 'E', 'W')
        val startDirs = directions.filter { dir ->
            when (dir) {
                'S' -> input.getOrNull(startPos.x + 1)?.get(startPos.y) in setOf('|', 'J', 'L')
                'N' -> input.getOrNull(startPos.x - 1)?.get(startPos.y) in setOf('|', 'F', '7')
                'E' -> input[startPos.x].getOrNull(startPos.y + 1) in setOf('-', 'J', '7')
                'W' -> input[startPos.x].getOrNull(startPos.y - 1) in setOf('-', 'F', 'L')
                else -> false
            }
        }

        val pipePositions = generateSequence(State(startPos, startDirs.first())) { (currentPos, currentDir) ->
            val currentPipe = input[currentPos.x][currentPos.y]
            val newDir = pipeToDirection[currentPipe]?.get(currentDir) ?: error("Invalid pipe / direction")
            val newPos = currentPos + directionToOffset[newDir]!!
            if (newPos == startPos) null else State(newPos, newDir)
        }.map { it.position }.toSet()

        // Cleanup initial grid
        val cleanGrid = input.mapIndexed { rowIndex, row ->
            row.mapIndexed { colIndex, value ->
                if (Pos(rowIndex, colIndex) in pipePositions) value else '.'
            }
        }.toList()

        // Double grid, add - and | to connect the pipe where necessary
        val doubledGrid = cleanGrid.flatMap { row ->
            listOf(row, row.map {
                if (it in "|7F" || (it == 'S' && 'S' in startDirs)) '|' else '.'
            })
        }.map { col ->
            col.flatMap {
                listOf(it, if (it in "-FL" || (it == 'S' && 'E' in startDirs)) '-' else '.')
            }
        }

        val queue = ArrayDeque(getOutlinePositions(doubledGrid).filter { (i, j) -> doubledGrid[i][j] == '.' }
            .map { (i, j) -> Pos(i, j) })
        val visited = queue.toMutableSet()

        while (queue.isNotEmpty()) {
            val (currentI, currentJ) = queue.removeFirst()

            listOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1)
                .map { (offsetI, offsetJ) -> currentI + offsetI to currentJ + offsetJ }
                .filter { (nextI, nextJ) ->
                    nextI in doubledGrid.indices
                            && nextJ in doubledGrid[nextI].indices
                            && (Pos(nextI, nextJ)) !in visited
                            && doubledGrid[nextI][nextJ] == '.'
                }.forEach { (nextI, nextJ) ->
                    visited.add(Pos(nextI, nextJ))
                    queue.add(Pos(nextI, nextJ))
                }
        }

        val innies = doubledGrid.indices.flatMap { i ->
            doubledGrid[i].indices.mapNotNull { j ->
                if (Pos(i, j) in visited || doubledGrid[i][j] != '.' || i % 2 == 1 || j % 2 == 1) null
                else 1
            }
        }.sum()

//        doubledGrid.joinToString("\n") { it.joinToString("") }.println()

        return innies
    }

//    val input = readInput("day-10-example")
    val input = readInput("day-10-input")
//    part1(input).println() //8
//    part2(input).println() //10
    part2InitialComplicatedIdea(input).println() //10
}
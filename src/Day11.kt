import java.util.*
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Int {
        val expanded = input.flatMap { row -> if (row.all { it == '.' }) listOf(row, row) else listOf(row) }
            .map { col ->
                col.flatMapIndexed { index, c ->
                    if (input.all { it[index] == '.' }) listOf(c, c) else listOf(
                        c
                    )
                }
            }

        var sum = 0
        for (i in expanded.indices) {
            for (j in expanded[i].indices) {
                if (expanded[i][j] == '#') {
                    val queue = ArrayDeque<Pair<Pair<Int, Int>, Int>>()
                    queue.add(i to j to 0)
                    val visited = mutableSetOf(i to j)

                    val distances = mutableMapOf(i to j to 0)

                    while (queue.isNotEmpty()) {
                        val (currentPos, currentDist) = queue.removeFirst()

                        for ((offsetI, offsetJ) in listOf(-1 to 0, 0 to -1, 1 to 0, 0 to 1)) {
                            val nextI = currentPos.first + offsetI
                            val nextJ = currentPos.second + offsetJ

                            if (nextI in expanded.indices
                                && nextJ in expanded[nextI].indices
                                && Pair(nextI, nextJ) !in visited
                            ) {
                                if (expanded[nextI][nextJ] == '#') {
                                    distances[nextI to nextJ] = currentDist + 1
                                }

                                queue.add(nextI to nextJ to currentDist + 1)
                                visited.add(nextI to nextJ)
                            }
                        }
                    }

                    sum += distances.values.sum()
                }
            }
        }

        return sum / 2
    }


    fun part2(input: List<String>): Long {
        val expandedRows = input.mapIndexedNotNull { index, row -> if (row.all { it == '.' }) index else null }.toSet()
        val expandedCols = input.first().indices.filter { index ->
            input.all { it[index] == '.' }
        }.toSet()


        val galaxyPositions =
            input.flatMapIndexed { i, col -> col.mapIndexedNotNull { j, c -> if (c == '#') (i to j) else null } }
                .toList()

        val pairs =
            galaxyPositions.flatMapIndexed { i, gp1 -> galaxyPositions.drop(i + 1).map { gp2 -> gp1 to gp2 } }

        val sum = pairs.sumOf { (gp1, gp2) ->
            abs(gp1.first - gp2.first) +
                    abs(gp1.second - gp2.second) +
                    expandedRows.count { min(gp1.first, gp2.first) < it && it < max(gp1.first, gp2.first) } * 999999L +
                    expandedCols.count {
                        min(gp1.second, gp2.second) < it && it < max(
                            gp1.second,
                            gp2.second
                        )
                    } * 999999L
        }

        return sum
    }

//    val input = readInput("day-11-example")
    val input = readInput("day-11-input")
//    part1(input).println() //374
    part2(input).println() //8410
}
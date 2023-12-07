import kotlin.math.max

fun main() {
    fun part1(input: List<String>): Long {
        var sum: Long = 0
        for (line in input) {
            val (cardName, rest) = line.split(':')
            val (winning, hand) = rest.trim().split('|')
            val winningSet = winning.trim().replace("  ", " ").split(' ').toSet()
            val handSet = hand.trim().replace("  ", " ").split(' ').toSet()

            val intersection = (winningSet intersect handSet)
            val intersectionSize = intersection.size

            sum += max((1 shl (intersectionSize - 1)), 0)
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val counter: MutableMap<Int, Long> = mutableMapOf()
        for ((index, line) in input.withIndex()) {
            val (cardName, rest) = line.split(':')
            val (winning, hand) = rest.trim().split('|')
            val winningSet = winning.trim().replace("  ", " ").split(' ').toSet()
            val handSet = hand.trim().replace("  ", " ").split(' ').toSet()

            val intersection = (winningSet intersect handSet)
            val intersectionSize = intersection.size
            counter[index] = counter.getOrDefault(index, 0) + 1
            for (i in index + 1..index + intersectionSize) {
                counter[i] = (counter[i] ?: 0) + (counter[index] ?: 0)
            }

        }

        return counter.values.sum()
    }


//    val input = readInput("day-04-example")
    val input = readInput("day-04-input")
//    part1(input).println()
    part2(input).println()
}
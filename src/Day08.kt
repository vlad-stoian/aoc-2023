import java.math.BigInteger

fun main() {

    fun part1(input: List<String>): Long {
        val directions = input[0].trim().map { it }.toList()
        val regex = """(.+) = \((.+), (.+)\)""".toRegex()

        val mapping = input.drop(2).mapNotNull { regex.find(it) }.associate { matchResult ->
            val (from, left, right) = matchResult.destructured
            from to Pair(left, right)
        }.toMutableMap()

        var current = "AAA"
        var steps: Long = 0
        while (current != "ZZZ") {
            for (d in directions) {
                require(d == 'L' || d == 'R') { "Direction should be L or R" }
                current = if (d == 'L') {
                    mapping[current]!!.first
                } else {
                    mapping[current]!!.second
                }
                steps++
                if (current == "ZZZ") break
            }
        }
        return steps
    }

    fun part2(input: List<String>): BigInteger {
        val directions = input[0].trim().map { it }.toList()
        val regex = """(.+) = \((.+), (.+)\)""".toRegex()

        val mapping = input.drop(2).mapNotNull { regex.find(it) }.associate { matchResult ->
            val (from, left, right) = matchResult.destructured
            from to Pair(left, right)
        }.toMutableMap()

        val startingElements = mapping.keys.filter { it.endsWith('A') }.toMutableList()

        val cycles = startingElements.map { startingElement ->
            sequence {
                var element = startingElement
                for (direction in directions.repeatIndefinitely()) {
                    require(direction == 'L' || direction == 'R') { "Direction should be L or R" }
                    val (left, right) = mapping[element] ?: error("Mapping not found for $element")
                    element = if (direction == 'L') left else right
                    yield(element)
                }
            }.takeWhile { !it.endsWith('Z') }.count().toBigInteger() + BigInteger.ONE
        }

        return findLCMOfListOfNumbers(cycles)
    }

//    val input = readInput("day-08-example")
    val input = readInput("day-08-input")
//    part1(input).println() //6
    part2(input).println() //6
}
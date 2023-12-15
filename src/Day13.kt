import java.util.concurrent.ConcurrentHashMap

fun main() {

    fun <T> List<T>.split(predicate: (T) -> Boolean): List<List<T>> {
        return fold(mutableListOf<MutableList<T>>()) { acc, element ->
            if (predicate(element)) {
                if (acc.isEmpty()) acc.add(mutableListOf())
                acc.add(mutableListOf())
            } else {
                if (acc.isEmpty()) acc.add(mutableListOf())
                acc.last().add(element)
            }
            acc
        }
    }

    fun transpose(strings: List<String>): List<String> {
        if (strings.isEmpty()) return emptyList()
        val length = strings.first().length
        return (0 until length).map { row ->
            strings.map { it[row] }.joinToString("")
        }
    }

    fun findRowSplit(mirror: List<String>, errors: Int): Long {
        var sum = 0L
        for (i in 1 until mirror.size) {
            val before = mirror.take(i)
            val after = mirror.drop(i)

            val nonMatching =
                (after zip before.reversed()).sumOf { (r1, r2) -> (r1 zip r2).count { (c1, c2) -> c1 != c2 } }

            if (nonMatching == errors) {
                sum += i
            }
        }

        return sum
    }

    fun part1(input: List<String>): Long {
        var sum = 0L
        val mirrors = input.split { it.isBlank() }.filter { it.isNotEmpty() }

        for (mirror in mirrors) {
            sum += 100 * findRowSplit(mirror, 0)
            sum += findRowSplit(transpose(mirror), 0)

        }
        return sum
    }


    fun part2(input: List<String>): Long {
        var sum = 0L
        val mirrors = input.split { it.isBlank() }.filter { it.isNotEmpty() }

        for (mirror in mirrors) {
            sum += 100 * findRowSplit(mirror, 1)
            sum += findRowSplit(transpose(mirror), 1)

        }
        return sum
    }

//    val input = readInput("day-13-example")
    val input = readInput("day-13-input")
//    part1(input).println() //405
    part2(input).println() //400
}
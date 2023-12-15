fun main() {

    val magicCache = mutableMapOf<Pair<String, List<Long>>, Long>()
    fun countPossibilities(springs: String, groups: List<Long>): Long {
        if ((springs to groups) in magicCache) {
            return magicCache[springs to groups]!!
        }
        if (groups.isEmpty()) {
            if (springs.all { it in setOf('.', '?') }) {
                return 1
            }
            return 0
        }
        val firstGroupSize = groups.first().toInt()
        val remainingGroups = groups.drop(1)
        // All the springs + all the gaps
        val remainingSpaces = remainingGroups.size + remainingGroups.sum().toInt()

        var possibilities = 0L

        for (i in 0 until (springs.length - remainingSpaces - firstGroupSize + 1)) {
            val possibleSprings = ".".repeat(i) + "#".repeat(firstGroupSize) + '.'
            val isMatching = (possibleSprings zip springs).all { (p, s) -> p == s || s == '?' }

            if (isMatching) {
                val remainingSprings = springs.drop(possibleSprings.length)
                val subPossibilities = countPossibilities(remainingSprings, remainingGroups)
                magicCache[remainingSprings to remainingGroups] = subPossibilities
                possibilities += subPossibilities
            }
        }

        return possibilities
    }

    fun part1(input: List<String>): Long {
        val mappings = input.map { it.split(" ") }.map { it -> it[0] to it[1].split(",").map { it.toLong() } }
        var arrangements = 0L
        for ((springs, groups) in mappings) {
            arrangements += countPossibilities(springs, groups)
        }
        return arrangements
    }


    fun part2(input: List<String>): Long {
        val mappings = input.map { it.split(" ") }.map { it[0] to it[1].split(",").map { it.toLong() } }
        var arrangements = 0L
        for ((springs, groups) in mappings) {
            val fiveSprings = List(5) { springs }.joinToString("?")
            val fiveGroups = List(5) { groups }.flatten()
            arrangements += countPossibilities(fiveSprings, fiveGroups)
        }
        return arrangements
    }

//    val input = readInput("day-12-example")
    val input = readInput("day-12-input")
//    part1(input).println() //21
    part2(input).println() //525152
}
fun main() {

    fun part1(input: List<String>): Long {
        var sum = 0L

        for (line in input) {
            val values = line.split(" ").map { it.toLong() }
            val lastTerms = mutableListOf(values.last())

            var differences = values
            while (true) {
                differences = differences.zipWithNext { a, b -> b - a }
                lastTerms.add(differences.last())
                if (differences.distinct().size == 1) break;
            }

            sum += lastTerms.sum()
        }
        return sum
    }


    fun part2(input: List<String>): Long {
        var sum = 0L

        for (line in input) {
            val values = line.split(" ").map { it.toLong() }
            val firstTerms = mutableListOf(values.first())

            var differences = values
            while (true) {
                differences = differences.zipWithNext { a, b -> b - a }
                firstTerms.add(differences.first())
                if (differences.distinct().size == 1) break;
            }

            sum += firstTerms.reduceRight { acc, num -> acc - num }
        }
        return sum

    }

//    val input = readInput("day-09-example")
    val input = readInput("day-09-input")
//    part1(input).println() //114
    part2(input).println() //2
}
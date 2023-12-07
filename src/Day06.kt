import java.math.BigInteger

fun main() {
    fun part1(input: List<String>): Long {
        var (timeHeader, restTime) = input[0].split(':')
        var (distanceHeader, restDistance) = input[1].split(':')

        var times = restTime.split(' ').filter { x -> x.isNotBlank() }.map { x -> x.toInt() }
        var distances = restDistance.split(' ').filter { x -> x.isNotBlank() }.map { x -> x.toInt() }

        var waysTotal: Long = 1
        for ((t, d) in times zip distances) {
            var ways: Long = 0
            for (pressFor in 0..t) {
                if ((t - pressFor) * pressFor > d) {
                    ways++
                }
            }
            waysTotal *= ways
        }

        return waysTotal
    }

    fun part2(input: List<String>): Long {
        var (timeHeader, restTime) = input[0].split(':')
        var (distanceHeader, restDistance) = input[1].split(':')

        var time = restTime.split(' ').filter { x -> x.isNotBlank() }.joinToString("").toBigInteger()
        var distance = restDistance.split(' ').filter { x -> x.isNotBlank() }.joinToString("").toBigInteger()


        var ways: Long = 0
        for (pressFor in BigInteger.ZERO..time) {
            if ((time - pressFor) * pressFor > distance) {
                ways++
            }
        }


        return ways
    }


//    val input = readInput("day-06-example")
    val input = readInput("day-06-input")
//    part1(input).println()
    part2(input).println()
}
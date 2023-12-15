fun main() {
    fun part1(input: List<String>): Int {
        return input.first().split(",").map { it.fold(0) { acc, c -> (acc + c.code) * 17 % 256 } }.sum()
    }


    fun part2(input: List<String>): Int {
        // MutableMap is implemented as a LinkedHashMap which preserves insertion order
        val boxes: List<MutableMap<String, Int>> = List(256) { mutableMapOf() }

        input.first().split(",").map {
            if (it.contains("=")) {
                val (label, focalLength) = it.split("=")
                label to focalLength.toInt()
            } else {
                it.split("-").first() to null
            }
        }.forEach {
            val key = it.first.fold(0) { acc, c -> (acc + c.code) * 17 % 256 }
            if (it.second == null) {
                boxes[key].remove(it.first)
            } else {
                boxes[key][it.first] = it.second!!
            }
        }
        val sum = boxes.withIndex().sumOf { (index, box) ->
            box.entries.withIndex().sumOf { it.value.value * (index + 1) * (it.index + 1) }
        }
        return sum
    }

//    val input = readInput("day-15-example")
    val input = readInput("day-15-input")
//    part1(input).println() //1320
    part2(input).println() //145
}
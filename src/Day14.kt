fun main() {
    fun rotateClockwise(map: MutableList<MutableList<Char>>): MutableList<MutableList<Char>> {
        val newMap = mutableListOf<MutableList<Char>>()
        for (i in 0 until map[0].size) {
            val row = mutableListOf<Char>()
            for (j in map.size - 1 downTo 0) {
                row.add(map[j][i])
            }
            newMap.add(row)
        }
        return newMap
    }

    fun moveRocksNorth(map: MutableList<MutableList<Char>>) {
        for (j in map.first().indices) {
            var firstFreeSpace = 0
            for (i in map.indices) {
                when (map[i][j]) {
                    '.' -> continue
                    '#' -> firstFreeSpace = i + 1
                    'O' -> {
                        if (i != firstFreeSpace) {
                            map[firstFreeSpace][j] = 'O'
                            map[i][j] = '.'
                        }

                        while (firstFreeSpace <= i && (map[firstFreeSpace][j] == 'O' || map[firstFreeSpace][j] == '#')) {
                            firstFreeSpace++
                        }
                    }
                }
            }
        }
    }


    fun calculateLoad(map: MutableList<MutableList<Char>>): Int {
        return map.withIndex()
            .flatMap { (i, row) -> row.withIndex().map { (j, c) -> if (c == 'O') map.size - i else 0 } }
            .sum()
    }


    fun part1(input: List<String>): Int {
        val map = input.map { it.toMutableList() }.toMutableList()
        moveRocksNorth(map)
        return calculateLoad(map)
    }


    fun part2(input: List<String>): Long {
        val stateToLoad = mutableMapOf<String, Long>()
        val stateToSteps = mutableMapOf<String, Long>()

        var map = input.map { it.toMutableList() }.toMutableList()
        var steps = 0L

        while (true) {
            for (i in 0 until 4) {
                moveRocksNorth(map)
                map = rotateClockwise(map)
            }

            val load = calculateLoad(map).toLong()
            val key = map.joinToString("\n") { it.joinToString("") }

            if (key in stateToSteps) {
                val currentStep = steps
                val nextStep = stateToSteps[key]!!
                val position = (1000000000L - nextStep) % (currentStep - nextStep) + nextStep - 1
                val theState = stateToSteps.filterValues { it == position }.keys.first()
                return stateToLoad[theState]!!
            }

            stateToLoad[key] = load
            stateToSteps[key] = steps

            steps++
        }
        return -1
    }

//    val input = readInput("day-14-example")
    val input = readInput("day-14-input")
//    part1(input).println() //136
    part2(input).println() //64
}
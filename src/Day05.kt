import kotlin.math.max
import kotlin.math.min

fun main() {
    data class Mapping(val dest: Long, val source: Long, val length: Long)

    fun part1(input: List<String>): Long {
        val seeds = input[0].substringAfter(":").trim().split(" ").map { it.toLong() }
        var mappingsList: MutableList<MutableList<Mapping>> = mutableListOf()

        var currentMappingList: MutableList<Mapping> = mutableListOf()
        for (line in input.drop(2)) {
            if (':' in line) {
                currentMappingList = mutableListOf()
                continue
            }
            if (line.isBlank()) {
                if (currentMappingList.isNotEmpty()) {
                    currentMappingList.sortBy { it.source }
                    mappingsList.add(currentMappingList)
                }
                continue
            }
            val (dest, source, length) = line.split(' ')
            currentMappingList.add(Mapping(dest.toLong(), source.toLong(), length.toLong()))
        }

        var currentLocation = seeds.toMutableList()
        currentLocation.println()
        for (currentMappings in mappingsList) {
            for ((index, location) in currentLocation.withIndex()) {
                for (currentMapping in currentMappings) {
                    if (location >= currentMapping.source && location < currentMapping.source + currentMapping.length) {
                        currentLocation[index] = currentMapping.dest + (location - currentMapping.source)
                        break
                    }
                }
            }
            currentLocation.println()
        }

        return currentLocation.min()
    }

    fun part2(input: List<String>): Long {
        val seeds = input[0].substringAfter(":").trim().split(" ").map { it.toLong() }
            .chunked(2) { (start, length) -> start until start + length }
        var mappingsList: MutableList<MutableList<Mapping>> = mutableListOf()

        var currentMappingList: MutableList<Mapping> = mutableListOf()
        for (line in input.drop(2)) {
            if (':' in line) {
                currentMappingList = mutableListOf()
                continue
            }
            if (line.isBlank()) {
                if (currentMappingList.isNotEmpty()) {
                    currentMappingList.sortBy { it.source }
                    mappingsList.add(currentMappingList)
                }
                continue
            }
            val (dest, source, length) = line.split(' ')
            currentMappingList.add(Mapping(dest.toLong(), source.toLong(), length.toLong()))
        }

        var currentLocations = seeds.toMutableList()
        for (currentMappings in mappingsList) {
            val nextLocations = mutableListOf<LongRange>()
            for (currentLocation in currentLocations) {
                var location = currentLocation
                for (currentMapping in currentMappings) {
                    val sourceFirst = currentMapping.source
                    val sourceLast = currentMapping.source + currentMapping.length - 1
                    val destFirst = currentMapping.dest
                    val destLast = currentMapping.dest + currentMapping.length - 1

                    if (location.last < sourceFirst) {
                        nextLocations.add(location)
                        break
                    }
                    if (location.first > sourceLast) {
                        continue
                    }

                    // split in 3, check if all 3 valid
                    val firstSplit = LongRange(location.first, sourceFirst - 1)
                    if (firstSplit.last >= firstSplit.first) {
                        nextLocations.add(firstSplit)
                    }

                    val secondSplit =
                        LongRange(max(location.first, sourceFirst), min(location.last, sourceLast))
                    // Transform first
                    nextLocations.add(
                        LongRange(
                            destFirst + secondSplit.first - sourceFirst,
                            destLast + secondSplit.last - sourceLast
                        )
                    )

                    val thirdSplit = LongRange(sourceLast + 1, location.last)
                    location = thirdSplit
                    if (thirdSplit.last >= thirdSplit.first) {
                        continue
                    }
                    break
                }
                if (location.last >= location.first) {
                    nextLocations.add(location)
                }
            }

            currentLocations = nextLocations
        }

        return currentLocations.minOf { it.first }
    }

//    val input = readInput("day-05-example")
    val input = readInput("day-05-input")
//    part1(input).println() // 35
    part2(input).println() // 46
}
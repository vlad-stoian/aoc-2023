fun main() {
    fun computeType(hand: String): Int {
        val eachCount = hand.groupingBy { it }.eachCount()
        return when (eachCount.size) {
            1 -> 7
            2 -> if (eachCount.containsValue(4)) 6 else 5
            3 -> if (eachCount.containsValue(3)) 4 else 3
            4 -> 2
            5 -> 1
            else -> throw RuntimeException("NO WAY")
        }
    }

    val cardToValue =
        mutableMapOf(
            *(2..9).map { it.digitToChar() to it }.toTypedArray(),
            'T' to 10, 'J' to 11, 'Q' to 12, 'K' to 13, 'A' to 14
        )

    fun computeHandValue(hand: String): Int {
        return hand.map { cardToValue.getValue(it) }.fold(0) { acc, i -> acc * 100 + i }
    }

    fun generateAll(hand: String): List<String> {
        return hand.map { it.toString() }.fold(listOf("")) { list, term ->
            when {
                term == "J" -> cardToValue.keys.filter { it != 'J' }.flatMap { all -> list.map { it + all } }
                else -> list.map { it + term }
            }
        }
    }

    val bestHandCache = hashMapOf<String, String>()
    fun bestHand(hand: String): String {
        return bestHandCache.getOrPut(hand) {
            generateAll(hand).maxWith(
                compareBy(
                    { computeType(it) },
                    { computeHandValue(it) })
            )
        }
    }

    fun part1(input: List<String>): Long {
        val handToBid = input.map { s -> s.split(' ') }.associate { s -> s.first() to s.last() }
        val hands = handToBid.keys

        val sortedHands = hands.sortedWith(compareBy({ computeType(it) }, { computeHandValue(it) }))

        return sortedHands.foldIndexed(0L) { index, acc, hand ->
            acc + (handToBid[hand]!!.toInt() * (index + 1))
        }
    }


    fun part2(input: List<String>): Long {
        cardToValue['J'] = 1
        val handToBid = input.map { s -> s.split(' ') }.associate { s -> s.first() to s.last() }
        val hands = handToBid.keys

        val sortedHands = hands.sortedWith(compareBy({ computeType(bestHand(it)) }, { computeHandValue(it) }))

        return sortedHands.foldIndexed(0L) { index, acc, hand ->
            acc + (handToBid[hand]!!.toInt() * (index + 1))
        }
    }

//    val input = readInput("day-07-example")
    val input = readInput("day-07-input")
//    part1(input).println() //6440
    part2(input).println() //5905
}
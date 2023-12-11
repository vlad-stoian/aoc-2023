import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

operator fun BigInteger.rangeTo(other: BigInteger) =
    BigIntegerRange(this, other)

class BigIntegerRange(
    override val start: BigInteger,
    override val endInclusive: BigInteger
) : ClosedRange<BigInteger>, Iterable<BigInteger> {
    override operator fun iterator(): Iterator<BigInteger> =
        BigIntegerRangeIterator(this)
}

class BigIntegerRangeIterator(
    private val range: ClosedRange<BigInteger>
) : Iterator<BigInteger> {
    private var current = range.start

    override fun hasNext(): Boolean =
        current <= range.endInclusive

    override fun next(): BigInteger {
        if (!hasNext()) {
            throw NoSuchElementException()
        }
        return current++
    }
}

fun arraysComparator(arr1: List<Int>, arr2: List<Int>): Int {
    require(arr1.size == arr2.size) { "Arrays must have equal size" }
    return (arr1 zip arr2).firstOrNull { (a, b) -> a != b }?.let { (a, b) -> a - b } ?: 0
}

tailrec fun findGCD(a: BigInteger, b: BigInteger): BigInteger = if (b == BigInteger.ZERO) a else findGCD(b, a % b)
fun findLCM(a: BigInteger, b: BigInteger): BigInteger {
    return a * b / findGCD(a, b)
}

fun findLCMOfListOfNumbers(numbers: List<BigInteger>): BigInteger {
    return numbers.reduce { acc, num -> findLCM(acc, num) }
}

fun <T> List<T>.repeatIndefinitely(): Sequence<T> = generateSequence { this }.flatten()

fun <T> getOutlineElements(grid: List<List<T>>): List<T> =
    if (grid.isEmpty() || grid.first().isEmpty()) emptyList()
    else grid.first() +
            grid.drop(1).dropLast(1).map { it.last() } +
            grid.last().reversed() +
            grid.drop(1).dropLast(1).reversed().map { it.first() }

fun <T> getOutlinePositions(grid: List<List<T>>): List<Pair<Int, Int>> =
    if (grid.isEmpty() || grid.first().isEmpty()) emptyList()
    else (0 until grid.first().size).map { index -> Pair(0, index) } +
            (1 until grid.size - 1).map { index -> Pair(index, grid[index].size - 1) } +
            (0 until grid.last().size).map { index -> Pair(grid.size - 1, index) }.reversed() +
            (1 until grid.size - 1).map { index -> Pair(index, 0) }.reversed()

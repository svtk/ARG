package util

import java.util.Properties
import java.io.FileInputStream
import java.util.Collections

fun getKey(): String {
    val properties = Properties()
    properties.load(FileInputStream("local.properties"))
    return properties["key"].toString()
}

fun IntRange.shift(shift: Int) = IntRange(start + shift, end + shift)

fun IntRange.intersect(other: IntRange): IntRange {
    val maxStart = max(start, other.start)
    val minEnd = min(end, other.end)
    if (maxStart > minEnd) return IntRange.EMPTY
    return maxStart..minEnd
}

fun <T: Comparable<T>> min(vararg elements: T) = Collections.min(elements.toList())

fun <T: Comparable<T>> max(vararg elements: T) = Collections.max(elements.toList())
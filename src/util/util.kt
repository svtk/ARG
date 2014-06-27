package util

import java.util.Properties
import java.io.FileInputStream

fun getKey(): String {
    val properties = Properties()
    properties.load(FileInputStream("local.properties"))
    return properties["key"].toString()
}

fun IntRange.shift(shift: Int) = IntRange(start + shift, end + shift)

fun IntRange.intersect(other: IntRange): IntRange =
        (if (start > other.start) start else other.start)..(if (end < other.end) end else other.end)


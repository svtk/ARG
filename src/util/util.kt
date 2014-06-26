package util

import java.util.Properties
import java.io.FileInputStream

fun getKey(): String {
    val properties = Properties()
    properties.load(FileInputStream("local.properties"))
    return properties["key"].toString()
}
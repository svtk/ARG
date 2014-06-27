package builders.idealtextbuilder

import java.io.File

fun main(args: Array<String>) {
    var a =FB2IdealTextBuilder().getText(File("c:\\file.fb2"));
    println(a?.idealString)
}
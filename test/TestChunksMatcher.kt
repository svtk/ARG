package test.matcher

import java.io.File
import model.Chunk
import model.AudioPosition
import model.FileReference
import builders.idealtextbuilder.getFormattedText
import matcher.placeChunk
import model.FormattedText

fun main(args: Array<String>) {
    val formattedText = getFormattedText(File("books/prideandprejudice.fb2"))!!
//    formattedText.println()
//    return
    val chunks = File("books/chunksForPrideAndPrejudice.txt").readLines()

    for (chunk in chunks) {
        val textPosition = formattedText.placeChunk(Chunk(chunk, AudioPosition(FileReference("file"), 1), 10))
        println(chunk)
        if (textPosition == null) {
            println("|----")
        }
        else {
            for (paragraphPosition in textPosition.paragraphPositions) {
                val paragraph = formattedText.paragraphs[paragraphPosition.paragraphIndex]
                println("${paragraphPosition.paragraphIndex}|" + formattedText.getTextForParagraphRange(paragraph, paragraphPosition.rangeInParagraph))
            }
        }
        println()
    }
}

fun FormattedText.println() {
    for (paragraph in paragraphs) {
        println("${paragraph.index}|${paragraph.wordsRange}|${paragraph.getText()}")
    }
}
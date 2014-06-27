package test.matcher

import java.io.File
import model.Chunk
import model.AudioPosition
import model.FileReference
import builders.idealtextbuilder.getFormattedText
import matcher.placeChunk

fun main(args: Array<String>) {
    val formattedText = getFormattedText(File("picture_of_doryan_gray.fb2"))!!

    val s = "old men are ever capable of emotion I know you will laugh at me replied but I really can't Exhibit II have put too much of myself into it"

    val textPosition = formattedText.placeChunk(Chunk(s, AudioPosition(FileReference("file"), 1), 10))!!
    for (paragraphPosition in textPosition.paragraphPositions) {
        val paragraph = formattedText.paragraphs[paragraphPosition.paragraphIndex]
        println(formattedText.getTextForParagraphRange(paragraph, paragraphPosition.rangeInParagraph))
    }
}


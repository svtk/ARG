package matcher

import audio.recognizer.TextRecognizer
import java.io.File
import builders.textfromaudiobuilder.Audio2FlacConverter
import builders.textfromaudiobuilder.Cutter
import builders.idealtextbuilder.getFormattedText
import model.Chunk
import model.AudioPosition
import model.FileReference
val BOOK_DIR = "test\\book"

fun main(args: Array<String>) {
    Cutter().cut(File("C:\\Users\\Fearfall\\Downloads\\english.mp3"))
    val arrayOfFiles = File(BOOK_DIR).listFiles()!!.toList()
    val textRecognizer = TextRecognizer()
    val formattedText = getFormattedText(File("prideandprejudice.fb2"))!!
    for (file in arrayOfFiles) {
        try {
            if(file.extension=="mp3") {
                val value1 = file.path.trimTrailing("mp3") + "flac"
                Audio2FlacConverter().convert(file.path, value1)
                val flacFile = File(value1)
                val s = textRecognizer.getText(flacFile, 16000)!!
                println(file.name+"  "+ s)
                if(s.isEmpty())
                {
                    println("No text")
                    continue
                }
                val textPosition = formattedText.placeChunk(Chunk(s, AudioPosition(FileReference("file"), 1), 10))!!
                for (paragraphPosition in textPosition.paragraphPositions) {
                    val paragraph = formattedText.paragraphs[paragraphPosition.paragraphIndex]
                    println(formattedText.getTextForParagraphRange(paragraph, paragraphPosition.rangeInParagraph))
                }
                Thread.sleep(15000)
            }
        } catch(e: Exception) {
        }
    }
    File(BOOK_DIR).listFiles()!!.forEach { it.delete() }
}



package matcher

import audio.recognizer.TextRecognizer
import java.io.File
import builders.textfromaudiobuilder.Cutter
import builders.textfromaudiobuilder.Audio2FlacConverter

fun main(args: Array<String>) {
    Cutter().cut(File("C:\\Users\\Fearfall\\Downloads\\english.mp3"))
    val arrayOfFiles = File("test").listFiles()!!.toList()
    val textRecognizer = TextRecognizer()
    for (file in arrayOfFiles) {
        try {
            if(file.extension=="mp3") {
                val value1 = file.path.trimTrailing("mp3") + "flac"
                Audio2FlacConverter().convert(file.path, value1)
                val flacFile = File(value1)
                println(file.name+"  "+textRecognizer.getText(flacFile, 16000))
                Thread.sleep(2000)
            }
        } catch(e: Exception) {
        }
    }
}



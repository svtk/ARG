package audio.recognizer

import audio.recognizer.google.RecognizerChunked
import audio.recognizer.google.GSpeechResponseListener
import audio.recognizer.google.GoogleResponse
import java.io.File
import util.getKey

class TextRecognizer() {
    val key = getKey()

    public fun getText(flacFile: File, sampleRate: Int): String? {
        val recognizerChunked = RecognizerChunked(key)

        val result = StringBuilder()
        recognizerChunked.addResponseListener(object : GSpeechResponseListener {
            override fun onResponse(gr: GoogleResponse?) {
                val s = gr?.getResponse()
                result.append(s)
               // println(s)
            }
        })
        recognizerChunked.getRecognizedDataForFlac(flacFile, sampleRate)
        return result.toString()
    }
}
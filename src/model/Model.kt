package model
import java.io.File

data class FormattedText
(
        val idealString:String,
        val paragraph2TextPosition: Map<Int,Int>
)
data class AudioPosition
(
        val audioFile: File,
        val position:Int
)

data class Chunk
(
        val parsedString:String,
        val audioPosition:AudioPosition,
        val duration:Int
)

data class ParagraphPosition
(
        val paragraph:Int,
        val audioPosition:AudioPosition
)


abstract class AudioPosition2ParagraphMap {
    abstract fun getParagraph(fromAudio:AudioPosition):Int
    abstract fun getPosition(fromText:Int):Int
}

data class Book
(
    val curpos: Int,
    val curpar: Int,
    val map: AudioPosition2ParagraphMap,
    val textFile:File,
    val audioFiles:List<File>
)
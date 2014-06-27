package model

data class FileReference(val name: String)

data class AudioPosition(
        val audioFile: FileReference,
        val position: Int
)

data class Chunk(
        val parsedString: String,
        val audioPosition: AudioPosition,
        val duration: Int
)

data class ParagraphPosition(
        val paragraphIndex: Int,
        val rangeInParagraph: IntRange
)

data class TextPosition(
        //todo what is needed to FBReader?
        val paragraphPositions: List<ParagraphPosition>,
        val range: IntRange
)

abstract class AudioPosition2ParagraphMap {
    abstract fun getParagraph(fromAudio: AudioPosition): Int
    abstract fun getPosition(fromText: Int): Int
}

data class Book
(
        val curpos: Int,
        val curpar: Int,
        val map: AudioPosition2ParagraphMap,
        val textFile: FileReference,
        val audioFiles: List<FileReference>
)
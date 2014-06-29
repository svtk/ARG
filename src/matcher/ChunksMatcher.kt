package matcher

import model.*
import builders.idealtextbuilder.getWordsWithoutFormatting
import util.intersect
import util.shift
import util.min

public fun FormattedText.placeChunk(chunk: Chunk): TextPosition? {
    return findWords(getWordsWithoutFormatting(chunk.parsedString).map {it.text})
}

public fun FormattedText.findWords(words: List<String>): TextPosition? {
    val allWords = wordsWithoutFormatting

    fun getIntersection(startIndex: Int, endIndex: Int): Set<String> {
        val intersection = allWords.subList(startIndex, endIndex).map { it.text }.toHashSet()
        intersection.retainAll(words)
        return intersection
    }
    fun getLastWordIndex(startIndex: Int) = min(startIndex + words.size + 5, allWords.size)

    var maxIntersectionSize = 4
    val possibleFirstIndexes = arrayListOf<Int>()
    for ((index, word) in allWords.withIndices()) {
        val intersectionSize = getIntersection(index, getLastWordIndex(index)).size
        if (intersectionSize > maxIntersectionSize) {
            maxIntersectionSize = intersectionSize
            possibleFirstIndexes.clear()
            possibleFirstIndexes.add(index)
        }
        else if (intersectionSize == maxIntersectionSize) {
            possibleFirstIndexes.add(index)
        }
    }

    if (possibleFirstIndexes.isEmpty()) return null

    var firstWordIndex = possibleFirstIndexes[possibleFirstIndexes.size/2]

    fun printGuesses() {
        for (startIndex in possibleFirstIndexes) {
            val endIndex = getLastWordIndex(startIndex)
            println("${getIntersection(startIndex, endIndex).size} ${getTextForWordRange(startIndex..endIndex)}")
        }
    }
//    printGuesses()

    val foundRange = firstWordIndex..getLastWordIndex(firstWordIndex)
    val paragraphs = paragraphs.filter { !foundRange.intersect(it.wordsRange).isEmpty() }

    val chunkRange = getTextRangeByWordsRange(foundRange)

    val paragraphPositions = paragraphs.map {
        ParagraphPosition(it.index, it.rangeInIdealText.intersect(chunkRange).shift(-it.rangeInIdealText.start))
    }
    return TextPosition(paragraphPositions, chunkRange)
}
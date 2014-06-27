package matcher

import model.*
import builders.idealtextbuilder.getWordsWithoutFormatting
import util.intersect
import util.shift

public fun FormattedText.placeChunk(chunk: Chunk): TextPosition? {
    return findWords(getWordsWithoutFormatting(chunk.parsedString).map {it.text})
}

public fun FormattedText.findWords(words: List<String>): TextPosition? {
    var firstWordIndex = 0
    var maxIntersectionSize = 5
    val allWords = wordsWithoutFormatting
    for ((index, word) in allWords.withIndices()) {
        val endIndex = index + words.size + 3
        if (endIndex !in 0..allWords.size - 1) continue

        val intersection = allWords.subList(index, endIndex).map { it.text }.toHashSet()
        intersection.retainAll(words)
        if (intersection.size > maxIntersectionSize) {
            firstWordIndex = index
            maxIntersectionSize = intersection.size
        }
    }

    fun specifyWord(index: Int, word: String?): Int {
        for (i in listOf(1, -1, 2, -2, 3, -3, 4, -4)) {
            if (word == allWords[index + i].text) {
                return index + i
            }
        }
        return index
    }
    firstWordIndex = specifyWord(firstWordIndex, words.first)
    var lastWordIndex = firstWordIndex + words.size
    lastWordIndex = specifyWord(lastWordIndex, words.last)

    val paragraphs = paragraphs.filter { firstWordIndex in it.wordsRange || lastWordIndex in it.wordsRange }

    val chunkRange = allWords[firstWordIndex].range.start..allWords[lastWordIndex].range.end

    val paragraphPositions = paragraphs.map {
        ParagraphPosition(it.index, it.rangeInIdealText.intersect(chunkRange).shift(-it.rangeInIdealText.start))
    }
    return TextPosition(paragraphPositions, chunkRange)
}
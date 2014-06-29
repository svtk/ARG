package model

import util.shift

data class Paragraph(
        val index: Int,
        val wordsRange: IntRange,
        val rangeInIdealText: IntRange,
        val rangeInInitialText: IntRange
)

data class Word(val text: String, val range: IntRange)

data class FormattedText(
        val idealString: String,
        val wordsWithoutFormatting: List<Word>,
        val paragraphs: List<Paragraph>
) {
    fun getParagraphWords(paragraph: Paragraph) =
            wordsWithoutFormatting.subList(paragraph.wordsRange.start, paragraph.wordsRange.end)

    fun Paragraph.getText() = getTextForRange(rangeInIdealText)

    fun getTextForRange(range: IntRange) = idealString.substring(range)

    fun getTextForWordRange(wordRange: IntRange) = getTextForRange(getTextRangeByWordsRange(wordRange))

    fun getTextForParagraphRange(paragraph: Paragraph, range: IntRange) =
            getTextForRange(range.shift(paragraph.rangeInIdealText.start))

    fun getTextRangeByWordsRange(wordRange: IntRange) =
            wordsWithoutFormatting[wordRange.start].range.start..wordsWithoutFormatting[wordRange.end].range.end
}
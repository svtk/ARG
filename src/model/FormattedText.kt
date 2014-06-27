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

    fun getParagraphText(paragraph: Paragraph) = getTextForRange(paragraph.rangeInIdealText)

    fun getTextForRange(range: IntRange) = idealString.substring(range)

    fun getTextForParagraphRange(paragraph: Paragraph, range: IntRange) =
            getTextForRange(range.shift(paragraph.rangeInIdealText.start))

    fun getRangeInIdealTextByWordsRange(wordsRange: IntRange) =
            wordsWithoutFormatting[wordsRange.start].range.start..wordsWithoutFormatting[wordsRange.end].range.end
}
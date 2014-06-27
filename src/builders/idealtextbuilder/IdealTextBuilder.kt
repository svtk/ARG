package builders.idealtextbuilder

import model.FormattedText
import java.io.File
import org.xml.sax.XMLReader
import javax.xml.parsers.SAXParserFactory
import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.Attributes
import java.util.Arrays
import model.*
import util.shift

fun getFormattedText(fb2: File): FormattedText? {

    var factory = SAXParserFactory.newInstance()
    var saxParser = factory?.newSAXParser()
    val paragraphTextToPosition = arrayListOf<Pair<String, Int>>()

    var handler = object : DefaultHandler() {
        var isText = true

        override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
            if (qName.equalsIgnoreCase("p")) {
                isText = true;
            }
        }

        override fun characters(ch: CharArray?, start: Int, length: Int) {
            if (isText) {
                var s = ""
                for (i in start..start + length - 1 ) {
                    s += ch!![i]
                }
                if (s != "\n") {
                    paragraphTextToPosition.add(s to start)
                }
                isText = false;
            }
        }
    };

    saxParser?.parse(fb2, handler);
    return getFormattedText(paragraphTextToPosition)
}

fun getFormattedText(paragraphTextToPosition: List<Pair<String, Int>>): FormattedText {
    val resultText = StringBuilder()

    val paragraphs = arrayListOf<Paragraph>()
    val words = arrayListOf<Word>()

    for ((paragraphNumber, p) in paragraphTextToPosition.withIndices()) {
        val (text, startIndexInInitialText) = p

        val wordsInParagraph = getWordsWithoutFormatting(text, resultText.length)

        paragraphs.add(Paragraph(paragraphNumber,
                IntRange(words.size, words.size + wordsInParagraph.size),
                (0..text.length - 1).shift(resultText.length),
                (0..text.length - 1).shift(startIndexInInitialText)))
        words.addAll(wordsInParagraph)
        resultText append text
    }
    return FormattedText(resultText.toString(), words, paragraphs)
}

fun getWordsWithoutFormatting(text: String, offset: Int = 0): List<Word> {
    var prevDelimiter = true
    val words = arrayListOf<Word>()
    var currentWord = StringBuilder()
    var wordStartIndex = 0
    for ((index, c) in text.withIndices()) {
        val isDelimiter = c in setOf(
                ' ', '\n', '\t', ',', '.', ';', ':', '!', '?', '-', '*', '"', '\'', '[', ']', '#', '<', '>')
        if (isDelimiter) {
            if (!prevDelimiter) {
                val wordText = currentWord.toString()
                words.add(Word(wordText, (wordStartIndex..index - 1).shift(offset)))
                currentWord = StringBuilder()
            }
            wordStartIndex = index + 1
        } else {
            currentWord.append(Character.toLowerCase(c))
        }
        prevDelimiter = isDelimiter
    }
    val lastWord = currentWord.toString()
    if (!lastWord.isEmpty()) {
        words.add(Word(lastWord, (wordStartIndex..text.length - 1).shift(offset)))
    }
    return words
}


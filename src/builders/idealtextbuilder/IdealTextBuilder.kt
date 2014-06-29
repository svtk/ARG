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
    val lastSentenceParts = arrayListOf<Pair<String, Int>>()

    fun addLastSentence() {
        if (lastSentenceParts.isEmpty()) return
        val lastSentence = lastSentenceParts.map{ it.first }.joinToString("")
        paragraphTextToPosition.add(lastSentence to lastSentenceParts.first!!.second)
        lastSentenceParts.clear()
    }

    var handler = object : DefaultHandler() {
        var isText = true
        var theSameSentence = false

        override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
            if (qName.equalsIgnoreCase("p")) {
                isText = true;
            }
            else if (qName.equalsIgnoreCase("emphasis")) {
                isText = true
                theSameSentence = true
            }
        }


        override fun endElement(uri: String?, localName: String, qName: String) {
            if (qName.equalsIgnoreCase("p")) {
                theSameSentence = false
                addLastSentence()
            }
            else if (qName.equalsIgnoreCase("emphasis")) {
                isText = true
                theSameSentence = true
            }
        }

        override fun characters(ch: CharArray?, start: Int, length: Int) {
            if (isText) {
                var s = ""
                for (i in start..start + length - 1 ) {
                    s += ch!![i]
                }
                if (s != "\n") {
                    lastSentenceParts.add(s to start)
                }
                isText = false;
            }
        }
    };
    addLastSentence()
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


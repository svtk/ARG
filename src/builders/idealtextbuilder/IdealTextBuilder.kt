package builders.idealtextbuilder

import model.FormattedText
import java.io.File
import org.xml.sax.XMLReader
import javax.xml.parsers.SAXParserFactory
import org.xml.sax.helpers.DefaultHandler
import org.xml.sax.Attributes
import java.util.Arrays

class FB2IdealTextBuilder
{
    fun getText(fb2:File): FormattedText?{

        var factory = SAXParserFactory.newInstance()
        var saxParser = factory?.newSAXParser()
        var resultText = ""
        var paragraphNumber = 0;
        var map = hashMapOf<Int, Int>()
        var handler = object: DefaultHandler() {
            var isText = true

            override fun startElement(uri: String, localName: String, qName: String, attributes: Attributes) {
                if (qName.equalsIgnoreCase("p")) {
                    isText = true;
                }
            }

            override fun characters(ch: CharArray?, start: Int, length: Int) {
                if(isText)
                {
                    var s = ""
                    for(i in start..start+length-1 ){
                        s += ch!![i]
                    }
                    map.put(++paragraphNumber, start)

                    resultText += s
                    isText = false;
                }
            }
        };

        saxParser?.parse(fb2, handler);
        return FormattedText(resultText, map)
    }
}

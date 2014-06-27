package builders.textfromaudiobuilder

import java.io.File
import org.ffmpeg.android.FfmpegController
import org.apache.commons.cli.Options
import com.xuggle.xuggler.Converter
import com.xuggle.mediatool.IMediaTool
import javax.print.attribute.standard.Media

class Audio2FlacConverter
{
    fun convert(input:String, output:String)
    {
        val converter = Converter()
            // first define options
            val options: Options = converter.defineOptions()!!
            // And then parse them.
       // var s = array("-ss","00:00:30.00","-t", "25", "-i", input, "-acodec","copy", output)
            var cmdLine = converter.parseOptions(options!!, array(input, output));
            // Finally, run the converter.
            converter.run(cmdLine);
      //  println(ffe.encode(input, File("test.flac")))

    }
}
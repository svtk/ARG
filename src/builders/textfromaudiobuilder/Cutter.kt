package builders.textfromaudiobuilder

import java.io.File
import com.google.code.mp3fenge.Mp3Fenge
val BOOK_DIR = "test\\book\\"
class Cutter {
    val LENGTH = 10
    fun cut(input: File?)
    {
        var helper = Mp3Fenge(input);
        val i1 = helper.getMp3Info()!!.getTrackLength() /2
        for (i in 0..i1/ LENGTH) {
            /*var e2 = helper.getDataByTime(i*10*1000, (i*10+10)*1000);
            var mp3datas = ArrayList<ByteArray>();
            mp3datas.add(e2!!);
            FileUtil.generateFile(File("test\\"+i+".mp3"), mp3datas)*/
            try {

                var line:String? = ""
                //-i english.mp3 -t 10 -ss 00:00:20.00 -acodec copy bar-new.mp3 // command to be passed to ffmpeg (args order significant)
                var outputName = BOOK_DIR + input!!.name.trim(".mp3") + "%05d".format(i) +".mp3";
                var inputName = input.name;
                val command = "\"../ffmpeg/bin/ffmpeg.exe\"" + " -i " + inputName + " -ss " + (i * LENGTH).toString() + " -t " + LENGTH + " -acodec copy " + outputName
                var p = Runtime.getRuntime().exec(command)
            }
            catch (err:Exception) {
                err.printStackTrace();
            }
        }
    }
}
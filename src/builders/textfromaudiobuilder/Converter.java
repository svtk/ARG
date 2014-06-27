/*
package builders.textfromaudiobuilder;

import com.xuggle.xuggler.*;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xuggle.xuggler.io.URLProtocolManager;

public class Converter
{
    static
    {
        URLProtocolManager.getManager();
    }

    public Converter()
    {

    }

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private IContainer mIContainer = null;
    private IContainer mOContainer = null;

    */
/**
     * A set of {@link IStream} values for each stream in the input
     * {@link IContainer}.
     *//*

    private IStream[] mIStreams = null;
    */
/**
     * A set of {@link IStreamCoder} objects we'll use to decode audio and video.
     *//*

    private IStreamCoder[] mICoders = null;

    */
/**
     * A set of {@link IStream} objects for each stream we'll output to the output
     * {@link IContainer}.
     *//*

    private IStream[] mOStreams = null;
    */
/**
     * A set of {@link IStreamCoder} objects we'll use to encode audio and video.
     *//*

    private IStreamCoder[] mOCoders = null;


    */
/**
     * A set of {@link IAudioSamples} objects we'll use to hold decoded audio
     * data.
     *//*

    private IAudioSamples[] mISamples = null;
    */
/**
     * A set of {@link IAudioSamples} objects we'll use to hold
     * potentially-resampled audio data before we encode it.
     *//*

    private IAudioSamples[] mOSamples = null;

    */
/**
     * A set of {@link IAudioResampler} objects (one for each stream) we'll use to
     * resample audio if needed.
     *//*

    private IAudioResampler[] mASamplers = null;

    */
/**
     * Should we convert audio
     *//*

    private boolean mHasAudio = true;

    */
/**
     * Should we force an interleaving of the output
     *//*

    private final boolean mForceInterleave = true;

    */
/**
     * Should we attempt to encode 'in real time'
     *//*

    private boolean mRealTimeEncoder;

    private Long mStartClockTime;
    private Long mStartStreamTime;

    */
/**
     * Define all the command line options this program can take.
     *
     * @return The set of accepted options.
     *//*

    public Options defineOptions()
    {
        Options options = new Options();


        OptionBuilder.withArgName("ss");
        OptionBuilder.hasArg(true);

        OptionBuilder.withArgName("t");
        OptionBuilder.hasArg(true);

        OptionBuilder.withArgName("i");
        OptionBuilder.hasArg(true);

        Option acodec = OptionBuilder.create("acodec");

        OptionBuilder.withArgName("icodec");
        OptionBuilder.hasArg(true);
        OptionBuilder
                .withDescription("input audio codec  (e.g. \"libmp3lame\")");
        return options;
    }

    public CommandLine parseOptions(Options opt, String[] args)
            throws ParseException
    {
        CommandLine cmdLine = null;

        CommandLineParser parser = new GnuParser();

        cmdLine = parser.parse(opt, args);

        if (cmdLine.hasOption("help"))
        {
            HelpFormatter help = new HelpFormatter();
            help.printHelp("Xuggler [options] input_url output_url", opt);
            System.exit(1);
        }
        // Make sure we have only two left over args
        if (cmdLine.getArgs().length != 2)
            throw new ParseException("missing input or output url");

        return cmdLine;
    }

    */
/**
     * Get an integer value from a command line argument.
     *
     * @param cmdLine
     *          A parsed command line (as returned from
     *          {@link #parseOptions(Options, String[])}
     * @param key
     *          The key for an option you want.
     * @param defaultVal
     *          The default value you want set if the key is not present in
     *          cmdLine.
     * @return The value for the key in the cmdLine, or defaultVal if it's not
     *         there.
     *//*

    private int getIntOptionValue(CommandLine cmdLine, String key, int defaultVal)
    {
        int retval = defaultVal;
        String optValue = cmdLine.getOptionValue(key);

        if (optValue != null)
        {
            try
            {
                retval = Integer.parseInt(optValue);
            }
            catch (Exception ex)
            {
                log
                        .warn(
                                "Option \"{}\" value \"{}\" cannot be converted to integer; using {} instead",
                                new Object[]
                                        {
                                                key, optValue, defaultVal
                                        });
            }
        }
        return retval;
    }

    */
/**
     * Get a double value from a command line argument.
     *
     * @param cmdLine
     *          A parsed command line (as returned from
     *          {@link #parseOptions(Options, String[])}
     * @param key
     *          The key for an option you want.
     * @param defaultVal
     *          The default value you want set if the key is not present in
     *          cmdLine.
     * @return The value for the key in the cmdLine, or defaultVal if it's not
     *         there.
     *//*

    private double getDoubleOptionValue(CommandLine cmdLine, String key,
                                        double defaultVal)
    {
        double retval = defaultVal;
        String optValue = cmdLine.getOptionValue(key);

        if (optValue != null)
        {
            try
            {
                retval = Double.parseDouble(optValue);
            }
            catch (Exception ex)
            {
                log
                        .warn(
                                "Option \"{}\" value \"{}\" cannot be converted to double; using {} instead",
                                new Object[]
                                        {
                                                key, optValue, defaultVal
                                        });
            }
        }
        return retval;
    }

    */
/**
     * Open an initialize all Xuggler objects needed to encode and decode a video
     * file.
     *
     * @param cmdLine
     *          A command line (as returned from
     *          {@link #parseOptions(Options, String[])}) that specifies what
     *          files we want to process and how to process them.
     * @return Number of streams in the input file, or <= 0 on error.
     *//*


    int setupStreams(CommandLine cmdLine)
    {
        String inputURL = cmdLine.getArgs()[0];
        String outputURL = cmdLine.getArgs()[1];

        mHasAudio = !cmdLine.hasOption("ano");

        mRealTimeEncoder = cmdLine.hasOption("realtime");

        String acodec = cmdLine.getOptionValue("acodec");
        Integer time = Integer.valueOf(cmdLine.getOptionValue("t"));
        String containerFormat = cmdLine.getOptionValue("ss");
        String input = cmdLine.getOptionValue("i");


        // Should have everything now!
        int retval = 0;

        */
/**
         * Create one container for input, and one for output.
         *//*

        mIContainer = IContainer.make();
        mOContainer = IContainer.make();

        String cpreset = cmdLine.getOptionValue("cpreset");
        if (cpreset != null)
            Configuration.configure(cpreset, mOContainer);

        IContainerFormat iFmt = null;
        IContainerFormat oFmt = null;


*/
/*
        IContainerParameters parameters = IContainerParameters.make();
        mIContainer.setParameters(parameters);
*//*


        retval = mIContainer.open(inputURL, IContainer.Type.READ, iFmt);
        if (retval < 0)
            throw new RuntimeException("could not open url: " + inputURL);

        */
/**
         * If the user EXPLICITLY asked for a output container format, we'll try to
         * honor their request here.
         *//*

        if (containerFormat != null)
        {
            oFmt = IContainerFormat.make();
            */
/**
             * Try to find an output format based on what the user specified, or
             * failing that, based on the outputURL (e.g. if it ends in .flv, we'll
             * guess FLV).
             *//*

            retval = oFmt.setOutputFormat(containerFormat, outputURL, null);
            if (retval < 0)
                throw new RuntimeException("could not find output container format: "
                        + containerFormat);
        }

        */
/**
         * Open the output container for writing. If oFmt is null, we are telling
         * Xuggler to guess the output container format based on the outputURL.
         *//*

        retval = mOContainer.open(outputURL, IContainer.Type.WRITE, oFmt);
        if (retval < 0)
            throw new RuntimeException("could not open output url: " + outputURL);

        */
/**
         * Find out how many streams are there in the input container? For example,
         * most FLV files will have 2 -- 1 audio stream and 1 video stream.
         *//*

        int numStreams = mIContainer.getNumStreams();
        if (numStreams <= 0)
            throw new RuntimeException("not streams in input url: " + inputURL);

        */
/**
         * Here we create IStream, IStreamCoders and other objects for each input
         * stream.
         *
         * We make parallel objects for each output stream as well.
         *//*

        mIStreams = new IStream[numStreams];
        mICoders = new IStreamCoder[numStreams];
        mOStreams = new IStream[numStreams];
        mOCoders = new IStreamCoder[numStreams];
        mASamplers = new IAudioResampler[numStreams];
        mISamples = new IAudioSamples[numStreams];
        mOSamples = new IAudioSamples[numStreams];

        */
/**
         * Now let's go through the input streams one by one and explicitly set up
         * our contexts.
         *//*

        for (int i = 0; i < numStreams; i++)
        {
            */
/**
             * Get the IStream for this input stream.
             *//*

            IStream is = mIContainer.getStream(i);
            */
/**
             * And get the input stream coder. Xuggler will set up all sorts of
             * defaults on this StreamCoder for you (such as the audio sample rate)
             * when you open it.
             *
             * You can create IStreamCoders yourself using
             * IStreamCoder#make(IStreamCoder.Direction), but then you have to set all
             * parameters yourself.
             *//*

            IStreamCoder ic = is.getStreamCoder();

            */
/**
             * Find out what Codec Xuggler guessed the input stream was encoded with.
             *//*

            ICodec.Type cType = ic.getCodecType();

            mIStreams[i] = is;
            mICoders[i] = ic;
            mOStreams[i] = null;
            mOCoders[i] = null;
            mASamplers[i] = null;
            mISamples[i] = null;
            mOSamples[i] = null;

            if (cType == ICodec.Type.CODEC_TYPE_AUDIO && mHasAudio)
            {
                */
/**
                 * So it looks like this stream as an audio stream. Now we add an audio
                 * stream to the output container that we will use to encode our
                 * resampled audio.
                 *//*

                IStream os = mOContainer.addNewStream(i);

                */
/**
                 * And we ask the IStream for an appropriately configured IStreamCoder
                 * for output.
                 *
                 * Unfortunately you still need to specify a lot of things for
                 * outputting (because we can't really guess what you want to encode
                 * as).
                 *//*

                IStreamCoder oc = os.getStreamCoder();
                String apreset = cmdLine.getOptionValue("apreset");
                if (apreset != null)
                    Configuration.configure(apreset, oc);

                mOStreams[i] = os;
                mOCoders[i] = oc;

                */
/**
                 * First, did the user specify an audio codec?
                 *//*

                if (acodec != null)
                {
                    ICodec codec = null;
                    */
/**
                     * Looks like they did specify one; let's look it up by name.
                     *//*

                    codec = ICodec.findEncodingCodecByName(acodec);
                    if (codec == null || codec.getType() != cType)
                        throw new RuntimeException("could not find encoder: " + acodec);
                    */
/**
                     * Now, tell the output stream coder that it's to use that codec.
                     *//*

                    oc.setCodec(codec);
                }
                else
                {
                    */
/**
                     * Looks like the user didn't specify an output coder for audio.
                     *
                     * So we ask Xuggler to guess an appropriate output coded based on the
                     * URL, container format, and that it's audio.
                     *//*

                    ICodec codec = ICodec.guessEncodingCodec(oFmt, null, outputURL, null,
                            cType);
                    if (codec == null)
                        throw new RuntimeException("could not guess " + cType
                                + " encoder for: " + outputURL);
                    */
/**
                     * Now let's use that.
                     *//*

                    oc.setCodec(codec);
                }

                */
/**
                 * In general a IStreamCoder encoding audio needs to know: 1) A ICodec
                 * to use. 2) The sample rate and number of channels of the audio. Most
                 * everything else can be defaulted.
                 *//*


                */
/**
                 * If the user didn't specify a sample rate to encode as, then just use
                 * the same sample rate as the input.
                 *//*

                int sampleRate = ic.getSampleRate();
                oc.setSampleRate(sampleRate);
                */
/**
                 * If the user didn't specify a bit rate to encode as, then just use the
                 * same bit as the input.
                 *//*

                int abitrate = ic.getBitRate();
                if (abitrate == 0)
                    // some containers don't give a bit-rate
                    abitrate = 64000;
                oc.setBitRate(abitrate);

                */
/**
                 * If the user didn't specify the number of channels to encode audio as,
                 * just assume we're keeping the same number of channels.
                 *//*

                int channels = ic.getChannels();
                oc.setChannels(channels);

                */
/**
                 * And set the quality (which defaults to 0, or highest, if the user
                 * doesn't tell us one).
                 *//*

                oc.setGlobalQuality(0);

                */
/**
                 * Now check if our output channels or sample rate differ from our input
                 * channels or sample rate.
                 *
                 * If they do, we're going to need to resample the input audio to be in
                 * the right format to output.
                 *//*

                if (oc.getChannels() != ic.getChannels()
                        || oc.getSampleRate() != ic.getSampleRate())
                {
                    */
/**
                     * Create an audio resampler to do that job.
                     *//*

                    mASamplers[i] = IAudioResampler.make(oc.getChannels(), ic
                            .getChannels(), oc.getSampleRate(), ic.getSampleRate());
                    if (mASamplers[i] == null)
                    {
                        throw new RuntimeException(
                                "could not open audio resampler for stream: " + i);
                    }
                }
                else
                {
                    mASamplers[i] = null;
                }
                */
/**
                 * Finally, create some buffers for the input and output audio
                 * themselves.
                 *
                 * We'll use these repeated during the #run(CommandLine) method.
                 *//*

                mISamples[i] = IAudioSamples.make(1024, ic.getChannels());
                mOSamples[i] = IAudioSamples.make(1024, oc.getChannels());
            }
            else
            {
                log.warn("Ignoring input stream {} of type {}", i, cType);
            }

            */
/**
             * Now, once you've set up all the parameters on the StreamCoder, you must
             * open() them so they can do work.
             *
             * They will return an error if not configured correctly, so we check for
             * that here.
             *//*

            if (mOCoders[i] != null)
            {
                retval = mOCoders[i].open();
                if (retval < 0)
                    throw new RuntimeException(
                            "could not open output encoder for stream: " + i);
                retval = mICoders[i].open();
                if (retval < 0)
                    throw new RuntimeException(
                            "could not open input decoder for stream: " + i);
            }
        }

        */
/**
         * Pretty much every output container format has a header they need written,
         * so we do that here.
         *
         * You must configure your output IStreams correctly before writing a
         * header, and few formats deal nicely with key parameters changing (e.g.
         * video width) after a header is written.
         *//*

        retval = mOContainer.writeHeader();
        if (retval < 0)
            throw new RuntimeException("Could not write header for: " + outputURL);

        */
/**
         * That's it with setup; we're good to begin!
         *//*

        return numStreams;
    }

    */
/**
     * Close and release all resources we used to run this program.
     *//*

    void closeStreams()
    {
        int numStreams = 0;
        int i = 0;

        numStreams = mIContainer.getNumStreams();
        */
/**
         * Some video coders (e.g. MP3) will often "read-ahead" in a stream and keep
         * extra data around to get efficient compression. But they need some way to
         * know they're never going to get more data. The convention for that case
         * is to pass null for the IMediaData (e.g. IAudioSamples or IVideoPicture)
         * in encodeAudio(...) or encodeVideo(...) once before closing the coder.
         *
         * In that case, the IStreamCoder will flush all data.
         *//*

        for (i = 0; i < numStreams; i++)
        {
            if (mOCoders[i] != null)
            {
                IPacket oPacket = IPacket.make();
                do {
                    if (mOCoders[i].getCodecType() == ICodec.Type.CODEC_TYPE_AUDIO)
                        mOCoders[i].encodeAudio(oPacket, null, 0);
                    else
                        mOCoders[i].encodeVideo(oPacket, null, 0);
                    if (oPacket.isComplete())
                        mOContainer.writePacket(oPacket, mForceInterleave);
                } while (oPacket.isComplete());
            }
        }
        */
/**
         * Some container formats require a trailer to be written to avoid a corrupt
         * files.
         *
         * Others, such as the FLV container muxer, will take a writeTrailer() call
         * to tell it to seek() back to the start of the output file and write the
         * (now known) duration into the Meta Data.
         *
         * So trailers are required. In general if a format is a streaming format,
         * then the writeTrailer() will never seek backwards.
         *
         * Make sure you don't close your codecs before you write your trailer, or
         * we'll complain loudly and not actually write a trailer.
         *//*

        int retval = mOContainer.writeTrailer();
        if (retval < 0)
            throw new RuntimeException("Could not write trailer to output file");

        */
/**
         * We do a nice clean-up here to show you how you should do it.
         *
         * That said, Xuggler goes to great pains to clean up after you if you
         * forget to release things. But still, you should be a good boy or giral
         * and clean up yourself.
         *//*

        for (i = 0; i < numStreams; i++)
        {
            if (mOCoders[i] != null)
            {
                */
/**
                 * And close the input coder to tell Xuggler it can release all native
                 * memory.
                 *//*

                mOCoders[i].close();
            }
            mOCoders[i] = null;
            if (mICoders[i] != null)
            */
/**
             * Close the input coder to tell Xuggler it can release all native
             * memory.
             *//*

                mICoders[i].close();
            mICoders[i] = null;
        }

        */
/**
         * Tell Xuggler it can close the output file, write all data, and free all
         * relevant memory.
         *//*

        mOContainer.close();
        */
/**
         * And do the same with the input file.
         *//*

        mIContainer.close();

        */
/**
         * Technically setting everything to null here doesn't do anything but tell
         * Java it can collect the memory it used.
         *
         * The interesting thing to note here is that if you forget to close() a
         * Xuggler object, but also loose all references to it from Java, you won't
         * leak the native memory. Instead, we'll clean up after you, but we'll
         * complain LOUDLY in your logs, so you really don't want to do that.
         *//*

        mOContainer = null;
        mIContainer = null;
        mISamples = null;
        mOSamples = null;
        mOCoders = null;
        mICoders = null;
        mASamplers = null;
    }

    protected IAudioSamples alterAudioFrame(IAudioSamples audioFrame)
    {
        return audioFrame;
    }

    */
/**
     * Allow child class to override this method to alter the video frame before
     * it is rencoded and written. In this implementation the video frame is
     * passed through unmodified.
     *
     * @param videoFrame
     *          the source video frame to be modified
     *
     * @return the modified video frame
     *//*


    protected IVideoPicture alterVideoFrame(IVideoPicture videoFrame)
    {
        return videoFrame;
    }

    */
/**
     * Takes a given command line and decodes the input file, and encodes with new
     * parameters to the output file.
     *
     * @param cmdLine
     *          A command line returned from
     *          {@link #parseOptions(Options, String[])}.
     *//*

    public void run(CommandLine cmdLine)
    {
        */
/**
         * Setup all our input and outputs
         *//*

        setupStreams(cmdLine);

        */
/**
         * Create packet buffers for reading data from and writing data to the
         * conatiners.
         *//*

        IPacket iPacket = IPacket.make();
        IPacket oPacket = IPacket.make();

        */
/**
         * Keep some "pointers' we'll use for the audio we're working with.
         *//*

        IAudioSamples inSamples = null;
        IAudioSamples outSamples = null;
        IAudioSamples reSamples = null;

        int retval = 0;

        */
/**
         * And keep some convenience pointers for the specific stream we're working
         * on for a packet.
         *//*

        IStreamCoder ic = null;
        IStreamCoder oc = null;
        IAudioResampler as = null;
        IVideoResampler vs = null;
        IVideoPicture inFrame = null;
        IVideoPicture reFrame = null;

        */
/**
         * Now, we've already opened the files in #setupStreams(CommandLine). We
         * just keep reading packets from it until the IContainer returns <0
         *//*

        while (mIContainer.readNextPacket(iPacket) == 0)
        {
            */
/**
             * Find out which stream this packet belongs to.
             *//*

            int i = iPacket.getStreamIndex();
            int offset = 0;

            */
/**
             * Find out if this stream has a starting timestamp
             *//*

            IStream stream = mIContainer.getStream(i);
            long tsOffset = 0;
            if (stream.getStartTime() != Global.NO_PTS && stream.getStartTime() > 0
                    && stream.getTimeBase() != null)
            {
                IRational defTimeBase = IRational.make(1,
                        (int) Global.DEFAULT_PTS_PER_SECOND);
                tsOffset = defTimeBase.rescale(stream.getStartTime(), stream
                        .getTimeBase());
            }
            */
/**
             * And look up the appropriate objects that are working on that stream.
             *//*

            ic = mICoders[i];
            oc = mOCoders[i];
            as = mASamplers[i];
            inSamples = mISamples[i];
            reSamples = mOSamples[i];

            if (oc == null)
                // we didn't set up this coder; ignore the packet
                continue;

            */
/**
             * Find out if the stream is audio or video.
             *//*

            ICodec.Type cType = ic.getCodecType();

            if (cType == ICodec.Type.CODEC_TYPE_AUDIO && mHasAudio)
            {
                */
/**
                 * Decoding audio works by taking the data in the packet, and eating
                 * chunks from it to create decoded raw data.
                 *
                 * However, there may be more data in a packet than is needed to get one
                 * set of samples (or less), so you need to iterate through the byts to
                 * get that data.
                 *
                 * The following loop is the standard way of doing that.
                 *//*

                while (offset < iPacket.getSize())
                {
                    retval = ic.decodeAudio(inSamples, iPacket, offset);
                    if (retval <= 0)
                        throw new RuntimeException("could not decode audio.  stream: " + i);

                    if (inSamples.getTimeStamp() != Global.NO_PTS)
                        inSamples.setTimeStamp(inSamples.getTimeStamp() - tsOffset);

                    log.trace("packet:{}; samples:{}; offset:{}", new Object[]
                            {
                                    iPacket, inSamples, tsOffset
                            });

                    */
/**
                     * If not an error, the decodeAudio returns the number of bytes it
                     * consumed. We use that so the next time around the loop we get new
                     * data.
                     *//*

                    offset += retval;
                    int numSamplesConsumed = 0;
                    */
/**
                     * If as is not null then we know a resample was needed, so we do that
                     * resample now.
                     *//*

                    if (as != null && inSamples.getNumSamples() > 0)
                    {
                        retval = as.resample(reSamples, inSamples, inSamples
                                .getNumSamples());

                        outSamples = reSamples;
                    }
                    else
                    {
                        outSamples = inSamples;
                    }

                    */
/**
                     * Include call a hook to derivied classes to allow them to alter the
                     * audio frame.
                     *//*


                    outSamples = alterAudioFrame(outSamples);

                    */
/**
                     * Now that we've resampled, it's time to encode the audio.
                     *
                     * This workflow is similar to decoding; you may have more, less or
                     * just enough audio samples available to encode a packet. But you
                     * must iterate through.
                     *
                     * Unfortunately (don't ask why) there is a slight difference between
                     * encodeAudio and decodeAudio; encodeAudio returns the number of
                     * samples consumed, NOT the number of bytes. This can be confusing,
                     * and we encourage you to read the IAudioSamples documentation to
                     * find out what the difference is.
                     *
                     * But in any case, the following loop encodes the samples we have
                     * into packets.
                     *//*

                    while (numSamplesConsumed < outSamples.getNumSamples())
                    {
                        retval = oc.encodeAudio(oPacket, outSamples, numSamplesConsumed);
                        if (retval <= 0)
                            throw new RuntimeException("Could not encode any audio: "
                                    + retval);
                        */
/**
                         * Increment the number of samples consumed, so that the next time
                         * through this loop we encode new audio
                         *//*

                        numSamplesConsumed += retval;
                        log.trace("out packet:{}; samples:{}; offset:{}", new Object[]{
                                oPacket, outSamples, tsOffset
                        });

                        writePacket(oPacket);
                    }
                }

            }
            else
            {
                */
/**
                 * Just to be complete; there are other types of data that can show up
                 * in streams (e.g. SUB TITLE).
                 *
                 * Right now we don't support decoding and encoding that data, but youc
                 * could still decide to write out the packets if you wanted.
                 *//*

                log.trace("ignoring packet of type: {}", cType);
            }

        }

        // and cleanup.
        closeStreams();
    }

    private void writePacket(IPacket oPacket)
    {
        int retval;
        if (oPacket.isComplete())
        {
            if (mRealTimeEncoder)
            {
                delayForRealTime(oPacket);
            }
            */
/**
             * If we got a complete packet out of the encoder, then go ahead
             * and write it to the container.
             *//*

            retval = mOContainer.writePacket(oPacket, mForceInterleave);
            if (retval < 0)
                throw new RuntimeException("could not write output packet");
        }
    }

    */
/**
     * WARNING for those who want to copy this method and think it'll stream
     * for them -- it won't.  It doesn't interleave packets from non-interleaved
     * containers, so instead it'll write chunky data.  But it's useful if you
     * have previously interleaved data that you want to write out slowly to
     * a file, or, a socket.
     * @param oPacket the packet about to be written.
     *//*

    private void delayForRealTime(IPacket oPacket)
    {
        // convert packet timestamp to microseconds
        final IRational timeBase = oPacket.getTimeBase();
        if (timeBase == null || timeBase.getNumerator() == 0 ||
                timeBase.getDenominator() == 0)
            return;
        long dts = oPacket.getDts();
        if (dts == Global.NO_PTS)
            return;

        final long currStreamTime = IRational.rescale(dts,
                1,
                1000000,
                timeBase.getNumerator(),
                timeBase.getDenominator(),
                IRational.Rounding.ROUND_NEAR_INF);
        if (mStartStreamTime == null)
            mStartStreamTime = currStreamTime;

        // convert now to microseconds
        final long currClockTime = System.nanoTime()/1000;
        if (mStartClockTime == null)
            mStartClockTime = currClockTime;

        final long currClockDelta  = currClockTime - mStartClockTime;
        if (currClockDelta < 0)
            return;
        final long currStreamDelta = currStreamTime - mStartStreamTime;
        if (currStreamDelta < 0)
            return;
        final long streamToClockDeltaMilliseconds = (currStreamDelta - currClockDelta)/1000;
        if (streamToClockDeltaMilliseconds <= 0)
            return;
        try
        {
            Thread.sleep(streamToClockDeltaMilliseconds);
        }
        catch (InterruptedException e)
        {
        }
    }

    */
/**
     *
     * A simple test of xuggler, this program takes an input file, and outputs it
     * as an output file.
     *
     * @param args
     *          The command line args passed to this program.
     *//*


    public static void main(String[] args)
    {
        Converter converter = new Converter();

        try
        {
            // first define options
            Options options = converter.defineOptions();
            // And then parse them.
            CommandLine cmdLine = converter.parseOptions(options, args);
            // Finally, run the converter.
            converter.run(cmdLine);
        }
        catch (Exception exception)
        {
            System.err.printf("Error: %s\n", exception.getMessage());
        }
    }

}*/
